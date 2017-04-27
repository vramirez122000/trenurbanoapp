package com.trenurbanoapp.service.impl;

import com.trenurbanoapp.dao.StatsLogDao;
import com.trenurbanoapp.dao.StopDao;
import com.trenurbanoapp.model.*;
import com.trenurbanoapp.scraper.model.AssetPosition;
import com.trenurbanoapp.scraper.model.LatLng;
import com.trenurbanoapp.service.VehicleSnapshotAlgService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by victor on 4/23/14.
 */
public class VehicleSnapshotAlgServiceSubroute extends VehicleSnapshotAlgServiceBase implements VehicleSnapshotAlgService {

    private static final Logger log = LogManager.getLogger(VehicleSnapshotAlgServiceSubroute.class);

    private StatsLogDao statsLogDao;
    private StopDao stopDao;

    public void setStatsLogDao(StatsLogDao statsLogDao) {
        this.statsLogDao = statsLogDao;
    }

    public void setStopDao(StopDao stopDao) {
        this.stopDao = stopDao;
    }

    @Override
    public VehiclePositionView getVehicleSnapshotView(AssetPosition assetPosition) {
        VehiclePositionView view = new VehiclePositionView();
        view.setAssetId(assetPosition.getAssetId());
        view.setTrail(assetPosition.getTrail());
        view.setStatus(RunningStatus.forStatusCode(assetPosition.getStatus()));

        VehicleState vehicleState = vehicleStateDao.getVehicleState(assetPosition.getAssetId());
        if(vehicleState != null) {
            List<LatLng> currTrail = assetPosition.getTrail();
            if(currTrail.size() == 1) {
                currTrail = new ArrayList<>(currTrail);
                for (LatLng latLng : vehicleState.getTrail()) {
                    if(!latLng.equals(currTrail.get(0))) {
                        currTrail.add(latLng);
                        break;
                    }
                }
            }
            view.setTrail(assetPosition.getTrail());
            view.setWithinServiceArea(vehicleState.isWithinServiceArea());
            if(vehicleState.getLastTrailChange() != null) {
                view.setPositionChange(vehicleState.getLastTrailChange().getTime());
            }
            Set<SubrouteKey> possibleSubroutes = vehicleState.getPossibleSubroutesAsSet();
            view.setInRoute(possibleSubroutes.contains(vehicleState.getLastKnownSubroute()));
            if(vehicleState.hasLastKnownRoute()) {
                SubrouteKey subroute = vehicleState.getLastKnownSubroute();
                view.setRoute(subroute.getRoute());
                view.setDirection(subroute.getDirection());
            }

            view.setPossibleRoutes(Constants.COMMA_JOINER.join(possibleSubroutes.stream().map(SubrouteKey::getRoute).collect(Collectors.toSet())));
            view.getProps().put("subrouteM", String.valueOf(vehicleState.getSubrouteMeasure()));
            view.getProps().put("withinOrigin", String.valueOf(vehicleState.isWithinOrigin()));
            view.getProps().put("avgSpeed", String.valueOf(vehicleState.getAvgSpeed()));
        }
        Vehicle vehicle = vehicleDao.getVehicle(assetPosition.getAssetId());
        if(vehicle != null) {
            view.setVehicle(vehicle.getName());
        }
        return view;
    }

    @Override
    public void updateVehicleState(AssetPosition assetPosition) {

        VehicleStateContainer container = vehicleStateDao.getVehicleStateContainer(assetPosition.getAssetId());
        VehicleState v = container.getState();
        if(!passesPreliminaryChecks(assetPosition, v)) {
            return;
        }


        List<LatLng> currTrail = assetPosition.getTrail();
        statsLogDao.insertAssetPosition(assetPosition);

        if(v.hasLastKnownRoute()) {
            Map<String, Object> withinOrigOrDest = subrouteDao.isWithinOriginOrDestination(currTrail, v.getLastKnownSubroute());
            if(withinOrigOrDest != null) {

                Boolean withinDest = (Boolean) withinOrigOrDest.get("withindest");
                String nextDirection = (String) withinOrigOrDest.get("next_direction");

                Boolean withinOrig = (Boolean) withinOrigOrDest.get("withinorig");
                if (withinDest || withinOrig && !v.isWithinOrigin()) {
                    //trip finish
                    if(v.getTripId() != null) {
                        statsLogDao.insertTripLog(v, null, currTrail);
                    }
                    v.setWithinOrigin(true);
                    if(withinDest && nextDirection != null) {
                        v.setLastKnownDirection(nextDirection);
                    }
                }

                if (!withinOrig && v.isWithinOrigin()) {
                    //trip start
                    v.setWithinOrigin(false);
                    v.setTripId(statsLogDao.nextTripId());
                    statsLogDao.insertTripLog(v, null, currTrail);
                }

                if (withinOrig || withinDest) {
                    v.getRecentSpeeds().clear();
                    v.setAvgSpeed(0F);
                    v.setTrail(currTrail);
                    v.setLastTrailChange(new Date());
                    v.setWithinServiceArea(true);
                    vehicleStateDao.updateVehicleState(v);
                    return;
                }
            } else {
                log.warn("no joins for subroute gid {}", v.getLastKnownSubroute());
            }
        }

        long secondsSinceMove = (assetPosition.getWhen().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - v.getLastTrailChange().getTime()) / 1000;
        if(v.getTripId() != null && secondsSinceMove < 120) {
            Stop closestStop = stopDao.getClosestStopWithin10Meters(assetPosition.getTrail());
            if(closestStop != null && (v.getStopId() == null || closestStop.getId() != v.getStopId())) {
                v.setStopId(closestStop.getId());
                statsLogDao.insertTripLog(v, closestStop.getId(), assetPosition.getTrail());
            }
        }

        //compare bearings, don't change route if bearing has changed too much
        double currBearing = calcBearing(v.getTrail().get(0), assetPosition.getTrail().get(0));
        if(v.getAzimuth() != null) {
            if (currBearing - v.getAzimuth() > .4 * Math.PI) {
                float distance = (float) calcDistance(currTrail.get(0), v.getTrail().get(0));
                v.addSpeed(distance / secondsSinceMove);
                v.setTrail(assetPosition.getTrail());
                v.setLastTrailChange(new Date());
                v.setWithinOrigin(false);
                v.setAzimuth((float) currBearing);
                vehicleStateDao.updateVehicleState(v);
                return;
            }
        }



        Map<SubrouteKey, SubrouteView> subrouteViews = subrouteDao.getGpsEnabledSubroutesWithin100Meters(currTrail.get(0), v.getTrail().get(0), container.getVehicle().getRoutes());
        Set<SubrouteKey> nearbySubroutes = subrouteViews.keySet();
        Set<SubrouteKey> oldPossibleSubroutes = v.getPossibleSubroutesAsSet();
        Set<SubrouteKey> newPossibleSubroutes = new HashSet<>(nearbySubroutes);

        //filter possible routes by configured allowed routes in vehicle table
        newPossibleSubroutes.retainAll(oldPossibleSubroutes);

        if (newPossibleSubroutes.size() == 1) {
            setSubroute(v, newPossibleSubroutes, currTrail);
        } else if (nearbySubroutes.size() == 1) {
            setSubroute(v, nearbySubroutes, currTrail);
        }

        else if (!v.hasLastKnownRoute()
                || (v.hasLastKnownRoute() && !newPossibleSubroutes.contains(v.getLastKnownSubroute()))
                || newPossibleSubroutes.isEmpty()) {
            v.updatePossibleSubroutes(nearbySubroutes);
        } else {
            v.updatePossibleSubroutes(newPossibleSubroutes);
        }

        if(v.hasLastKnownRoute() && subrouteViews.containsKey(v.getLastKnownSubroute())) {
            SubrouteView lastKnownSubrouteView = subrouteViews.get(v.getLastKnownSubroute());
            float currMeasure = (float) lastKnownSubrouteView.getSubrouteM();
            if (v.getSubrouteMeasure() != null && currMeasure < v.getSubrouteMeasure()) {
                log.warn("Decreasing m value for vehicle {} in subroute {} to {}",
                        container.getVehicle().getName(),
                        lastKnownSubrouteView.getSubrouteKey().getRoute(),
                        lastKnownSubrouteView.getSubrouteKey().getDirection());
            }
            v.setSubrouteMeasure(currMeasure);
        }

        if (secondsSinceMove > 0) {
            float distance = (float) calcDistance(currTrail.get(0), v.getTrail().get(0));
            float speedInMetersPerSecond = distance / secondsSinceMove;
            v.addSpeed(speedInMetersPerSecond);
        } else {
            v.addSpeed(0);
        }

        v.setTrail(currTrail);
        v.setLastTrailChange(Date.from(assetPosition.getWhen().atZone(ZoneId.systemDefault()).toInstant()));
        v.setWithinServiceArea(!nearbySubroutes.isEmpty());
        v.setWithinOrigin(false);
        v.setAzimuth((float) currBearing);
        vehicleStateDao.updateVehicleState(v);
    }

    private void setSubroute(VehicleState v, Iterable<SubrouteKey> newSubrouteIds, List<LatLng> currTrail) {
        SubrouteKey subroute = newSubrouteIds.iterator().next();
        v.updatePossibleSubroutes(newSubrouteIds);
        if(subroute.equals(v.getLastKnownSubroute())) {
            return;
        }

        if(v.getTripId() != null) {
            statsLogDao.insertTripLog(v, null, currTrail);
        }
        v.setLastKnownSubroute(subroute);
        v.setTripId(statsLogDao.nextTripId());
        statsLogDao.insertTripLog(v,  null, currTrail);
    }

}
