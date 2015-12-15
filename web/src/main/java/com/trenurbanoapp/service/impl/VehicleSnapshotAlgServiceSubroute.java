package com.trenurbanoapp.service.impl;

import com.google.common.collect.ImmutableSet;
import com.trenurbanoapp.dao.StatsLogDao;
import com.trenurbanoapp.dao.StopDao;
import com.trenurbanoapp.dao.SubrouteDao;
import com.trenurbanoapp.model.*;
import com.trenurbanoapp.scraper.model.AssetPosition;
import com.trenurbanoapp.scraper.model.LatLng;
import com.trenurbanoapp.service.VehicleSnapshotAlgService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by victor on 4/23/14.
 */
public class VehicleSnapshotAlgServiceSubroute extends VehicleSnapshotAlgServiceBase implements VehicleSnapshotAlgService {

    private static final Logger log = LogManager.getLogger(VehicleSnapshotAlgServiceSubroute.class);

    private StatsLogDao statsLogDao;
    private StopDao stopDao;
    public static final String[] DIRECTIONS = new String[]{
            "norte",
            "noreste",
            "noreste",
            "este",
            "este",
            "sureste",
            "sureste",
            "sur",
            "sur",
            "suroeste",
            "suroeste",
            "oeste",
            "oeste",
            "noroeste",
            "noroeste",
            "norte"
    };

    public void setStatsLogDao(StatsLogDao statsLogDao) {
        this.statsLogDao = statsLogDao;
    }

    public void setStopDao(StopDao stopDao) {
        this.stopDao = stopDao;
    }

    @Override
    public VehiclePositionView getVehicleSnapshotView(AssetPosition assetSnapshot) {
        VehiclePositionView view = new VehiclePositionView();
        view.setAssetId(assetSnapshot.getAssetId());
        view.setTrail(assetSnapshot.getTrail());
        view.setStatus(RunningStatus.forStatusCode(assetSnapshot.getStatus()));

        VehicleState vehicleState = vehicleStateDao.getVehicleState(assetSnapshot.getAssetId());
        if(vehicleState != null) {
            List<LatLng> currTrail = assetSnapshot.getTrail();
            if(currTrail.size() == 1) {
                currTrail = new ArrayList<>(currTrail);
                for (LatLng latLng : vehicleState.getTrail()) {
                    if(!latLng.equals(currTrail.get(0))) {
                        currTrail.add(latLng);
                        break;
                    }
                }
            }
            view.setTrail(assetSnapshot.getTrail());
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
        Vehicle vehicle = vehicleDao.getVehicle(assetSnapshot.getAssetId());
        if(vehicle != null) {
            view.setVehicle(vehicle.getName());
        }
        return view;
    }

    @Override
    public void updateVehicleState(AssetPosition assetSnapshot) {

        VehicleState v = vehicleStateDao.getVehicleState(assetSnapshot.getAssetId());
        if(!passesPreliminaryChecks(assetSnapshot, v)) {
            return;
        }

        List<LatLng> currTrail = assetSnapshot.getTrail();
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

        if(currTrail.size() == 1) {
            currTrail = new ArrayList<>(currTrail);
            for (LatLng latLng : v.getTrail()) {
                if(!latLng.equals(currTrail.get(0))) {
                    currTrail.add(latLng);
                    break;
                }
            }
        }
        if(currTrail.size() == 1) {
            return;
        }

        long secondsSinceMove = (System.currentTimeMillis() - v.getLastTrailChange().getTime()) / 1000;
        if(v.getTripId() != null && secondsSinceMove < 120) {
            Stop closestStop = stopDao.getClosestStopWithin10Meters(assetSnapshot.getTrail());
            if(closestStop != null && (v.getStopId() == null || closestStop.getId() != v.getStopId())) {
                v.setStopId(closestStop.getId());
                statsLogDao.insertTripLog(v, closestStop.getId(), assetSnapshot.getTrail());
            }
        }

        if(assetSnapshot.getTrail().size() > 1
                && v.getTrail().size() > 1) {
            //compare bearings, don't change route if bearing has changed too much
            double currBearing = calcBearing(assetSnapshot.getTrail().get(assetSnapshot.getTrail().size() - 1), assetSnapshot.getTrail().get(0));
            double prevBearing = calcBearing(v.getTrail().get(v.getTrail().size() - 1), v.getTrail().get(0));
            if(currBearing - prevBearing > .4 * Math.PI) {
                float speedInMetersPerSecond = calcSpeed(currTrail, v.getTrail(), v.getLastTrailChange());
                v.addSpeed(speedInMetersPerSecond);
                v.setTrail(assetSnapshot.getTrail());
                v.setLastTrailChange(new Date());
                v.setWithinOrigin(false);
                vehicleStateDao.updateVehicleState(v);
                return;
            }
        }

        Map<SubrouteKey, SubrouteView> subrouteViews = subrouteDao.getGpsEnabledSubroutesWithinDistance(currTrail, 30);
        Set<SubrouteKey> nearbySubroutes = subrouteViews.keySet();
        Set<SubrouteKey> oldPossibleSubroutes = v.getPossibleSubroutesAsSet();
        Set<SubrouteKey> newPossibleSubroutes = new HashSet<>(nearbySubroutes);
        newPossibleSubroutes.retainAll(oldPossibleSubroutes);

        SubrouteKey lastKnownSubroute = v.getLastKnownSubroute();

        if (newPossibleSubroutes.size() == 1) {
            setSubroute(v, newPossibleSubroutes, currTrail);
        } else if (nearbySubroutes.size() == 1) {
            setSubroute(v, nearbySubroutes, currTrail);
        }
        //todo add logic to switch subroutes at the end of the trip
        /*else if (v.hasLastKnownRoute()
                && !nearbySubroutes.contains(v.getLastKnownSubroute())
                && lastKnownSubroute != null
                && lastKnownSubroute.getNextSubrouteId() != null
                && nearbySubroutes.contains(lastKnownSubroute.getNextSubrouteId())) {
            setSubroute(v, Collections.singletonList(lastKnownSubroute.getNextSubrouteId()), currTrail);
        }*/
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
                Vehicle vehicle = vehicleDao.getVehicle(v.getAssetId());
                log.warn("Decreasing m value for vehicle {} in subroute {} to {}",
                        vehicle.getName(),
                        lastKnownSubrouteView.getSubroute().getRoute(),
                        lastKnownSubrouteView.getSubroute().getDirection());
            }
            v.setSubrouteMeasure(currMeasure);
        }

        float speedInMetersPerSecond = calcSpeed(currTrail, v.getTrail(), v.getLastTrailChange());
        v.addSpeed(speedInMetersPerSecond);

        v.setTrail(currTrail);
        v.setLastTrailChange(new Date());
        v.setWithinServiceArea(!nearbySubroutes.isEmpty());
        v.setWithinOrigin(false);
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
