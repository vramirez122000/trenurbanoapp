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
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.LocalDateTime;

import java.util.*;

/**
 * Created by victor on 4/23/14.
 */
public class VehicleSnapshotAlgServiceSubroute extends VehicleSnapshotAlgServiceBase implements VehicleSnapshotAlgService {

    private static final Logger log = LogManager.getLogger(VehicleSnapshotAlgServiceSubroute.class);

    private SubrouteDao subrouteDao;
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

    public void setSubrouteDao(SubrouteDao subrouteDao) {
        this.subrouteDao = subrouteDao;
    }

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
        if(assetSnapshot.getAssetId() == 1085) {
            System.out.println("");
        }
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
                view.setPositionChange(vehicleState.getLastTrailChange().toDate().getTime());
            }
            ImmutableSet<Integer> possibleSubroutes = vehicleState.getPossibleSubroutesAsSet();
            view.setInRoute(possibleSubroutes.contains(vehicleState.getLastKnownSubrouteId()));
            if(vehicleState.getLastKnownSubrouteId() != null) {
                Subroute subroute = subrouteDao.getSubroute(vehicleState.getLastKnownSubrouteId());
                view.setRoute(subroute.getRouteName());
                view.setDestination(subroute.getDestination());
            }

            List<String> possibleSubrouteNames = vehicleStateDao.getPossibleSubroutesNames(assetSnapshot.getAssetId());
            view.setPossibleRoutes(Constants.COMMA_JOINER.join(possibleSubrouteNames));
            view.getProps().put("subrouteM", String.valueOf(vehicleState.getSubrouteMeasure()));
            view.getProps().put("withinOrigin", String.valueOf(vehicleState.isWithinOrigin()));
            view.getProps().put("avgSpeed", String.valueOf(vehicleState.getAvgSpeed()));
            view.getProps().put("cardinal", String.valueOf(vehicleState.getCardinalDirection()));
        }
        Vehicle vehicle = vehicleDao.getVehicle(assetSnapshot.getAssetId());
        if(vehicle != null) {
            view.setVehicle(vehicle.getName());
        }
        return view;
    }

    @Override
    public void updateVehicleState(AssetPosition assetSnapshot) {

        if(assetSnapshot.getAssetId() == 834) {
            log.debug("here");
        }

        VehicleState v = vehicleStateDao.getVehicleState(assetSnapshot.getAssetId());
        if(!passesPreliminaryChecks(assetSnapshot, v)) {
            return;
        }

        List<LatLng> currTrail = assetSnapshot.getTrail();
        if(v.getLastKnownSubrouteId() != null) {
            Map<String, Object> withinOrigOrDest = subrouteDao.isWithinOriginOrDestination(currTrail, v.getLastKnownSubrouteId());
            if(withinOrigOrDest != null) {

                Boolean withinDest = (Boolean) withinOrigOrDest.get("withindest");
                Integer nextId = (Integer) withinOrigOrDest.get("nextid");

                Boolean withinOrig = (Boolean) withinOrigOrDest.get("withinorig");
                if (withinDest || withinOrig && !v.isWithinOrigin()) {
                    //trip finish
                    if(v.getTripId() != null) {
                        statsLogDao.insertTripLog(v, null, currTrail);
                    }
                    v.setWithinOrigin(true);
                    if(withinDest && nextId != null) {
                        v.setLastKnownSubrouteId(nextId);
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
                    v.setLastTrailChange(LocalDateTime.now());
                    v.setWithinServiceArea(true);
                    vehicleStateDao.updateVehicleState(v);
                    return;
                }
            } else {
                log.warn("no joins for subroute gid {}", v.getLastKnownSubrouteId());
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

        long secondsSinceMove = new Duration(v.getLastTrailChange().toDateTime(), DateTime.now()).getMillis() / 1000;
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
            double currBearing = bearing(assetSnapshot.getTrail().get(assetSnapshot.getTrail().size() - 1), assetSnapshot.getTrail().get(0));
            double prevBearing = bearing(v.getTrail().get(v.getTrail().size() - 1), v.getTrail().get(0));
            if(currBearing - prevBearing > .4 * Math.PI) {
                float speedInMetersPerSecond = calcSpeed(currTrail, v.getTrail(), v.getLastTrailChange());
                v.addSpeed(speedInMetersPerSecond);
                v.setTrail(assetSnapshot.getTrail());
                v.setLastTrailChange(LocalDateTime.now());
                v.setWithinOrigin(false);
                v.setCardinalDirection(bearingToCardinal(bearing(currTrail.get(currTrail.size() - 1), currTrail.get(0))));
                vehicleStateDao.updateVehicleState(v);
                return;
            }
        }

        List<LatLng> currTrailNad83 = CrsConversion.convertToNad83(currTrail);
        Map<Integer, SubrouteView> subrouteViews = subrouteDao.getGpsEnabledSubroutesWithinDistance(currTrailNad83, 30);
        Set<Integer> containingSubrouteIds = subrouteViews.keySet();
        ImmutableSet<Integer> oldPossibleSubroutes = v.getPossibleSubroutesAsSet();
        Set<Integer> newPossibleSubroute = new HashSet<>(containingSubrouteIds);
        newPossibleSubroute.retainAll(oldPossibleSubroutes);

        Subroute lastKnownSubroute = null;
        if(v.getLastKnownSubrouteId() != null) {
            lastKnownSubroute = subrouteDao.getSubroute(v.getLastKnownSubrouteId());
        }

        if (newPossibleSubroute.size() == 1) {
            setSubroute(v, newPossibleSubroute, currTrail);
        } else if (containingSubrouteIds.size() == 1) {
            setSubroute(v, containingSubrouteIds, currTrail);
        } else if (v.getLastKnownSubrouteId() != null
                && !containingSubrouteIds.contains(v.getLastKnownSubrouteId())
                && lastKnownSubroute != null
                && lastKnownSubroute.getNextSubrouteId() != null
                && containingSubrouteIds.contains(lastKnownSubroute.getNextSubrouteId())) {
            setSubroute(v, Arrays.asList(lastKnownSubroute.getNextSubrouteId()), currTrail);
        } else if (v.getLastKnownSubrouteId() == null
                || !newPossibleSubroute.contains(v.getLastKnownSubrouteId())
                || newPossibleSubroute.isEmpty()) {
            v.updatePossibleSubrouteIds(containingSubrouteIds);
        } else {
            v.updatePossibleSubrouteIds(newPossibleSubroute);
        }

        if(v.getLastKnownSubrouteId() != null && subrouteViews.containsKey(v.getLastKnownSubrouteId())) {
            SubrouteView lastKnownSubrouteView = subrouteViews.get(v.getLastKnownSubrouteId());
            float currMeasure = (float) lastKnownSubrouteView.getSubrouteM();
            if (v.getSubrouteMeasure() != null && currMeasure < v.getSubrouteMeasure()) {
                Vehicle vehicle = vehicleDao.getVehicle(v.getAssetId());
                log.warn("Decreasing m value for vehicle {} in subroute {} to {}",
                        vehicle.getName(),
                        lastKnownSubrouteView.getSubroute().getRouteName(),
                        lastKnownSubrouteView.getSubroute().getDestination());
            }
            v.setSubrouteMeasure(currMeasure);
        }

        float speedInMetersPerSecond = calcSpeed(currTrail, v.getTrail(), v.getLastTrailChange());
        v.addSpeed(speedInMetersPerSecond);

        v.setTrail(currTrail);
        v.setLastTrailChange(LocalDateTime.now());
        v.setWithinServiceArea(!containingSubrouteIds.isEmpty());
        v.setWithinOrigin(false);
        v.setCardinalDirection(bearingToCardinal(bearing(currTrail.get(currTrail.size() - 1), currTrail.get(0))));
        vehicleStateDao.updateVehicleState(v);
    }

    private void setSubroute(VehicleState v, Iterable<Integer> newSubrouteIds, List<LatLng> currTrail) {
        Integer newSubrouteId = newSubrouteIds.iterator().next();
        v.updatePossibleSubrouteIds(newSubrouteIds);
        if(newSubrouteId.equals(v.getLastKnownSubrouteId())) {
            return;
        }

        if(v.getTripId() != null) {
            statsLogDao.insertTripLog(v, null, currTrail);
        }
        v.setLastKnownSubrouteId(newSubrouteId);
        v.setTripId(statsLogDao.nextTripId());
        statsLogDao.insertTripLog(v,  null, currTrail);
    }

}
