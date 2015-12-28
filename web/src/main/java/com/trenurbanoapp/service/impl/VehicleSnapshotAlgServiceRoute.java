package com.trenurbanoapp.service.impl;

import com.trenurbanoapp.model.*;
import com.trenurbanoapp.scraper.model.AssetPosition;
import com.trenurbanoapp.scraper.model.LatLng;
import com.trenurbanoapp.service.VehicleSnapshotAlgService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.ZoneId;
import java.util.*;

/**
 * Created by victor on 4/23/14.
 */
public class VehicleSnapshotAlgServiceRoute extends VehicleSnapshotAlgServiceBase implements VehicleSnapshotAlgService {

    private static final Logger log = LogManager.getLogger(VehicleSnapshotAlgServiceRoute.class);

    @Override
    public VehiclePositionView getVehicleSnapshotView(AssetPosition assetPosition) {
        VehiclePositionView view = new VehiclePositionView();
        view.setAssetId(assetPosition.getAssetId());
        view.setTrail(assetPosition.getTrail());
        view.setStatus(RunningStatus.forStatusCode(assetPosition.getStatus()));
        VehicleState vehicleState = vehicleStateDao.getVehicleState(assetPosition.getAssetId());
        if(vehicleState != null) {
            view.setWithinServiceArea(vehicleState.isWithinServiceArea());
            if(vehicleState.getLastTrailChange() != null) {
                view.setPositionChange(vehicleState.getLastTrailChange().getTime());
            }
            Set<String> possibleRoutes = vehicleState.getPossibleRoutesAsSet();
            view.setInRoute(possibleRoutes.contains(vehicleState.getLastKnownRoute()));
            if(vehicleState.getLastKnownRoute() != null) {
                view.setRoute(vehicleState.getLastKnownRoute());
            }

            Set<String> possibleGeofenceRouteNames = vehicleStateDao.getPossibleRoutes(assetPosition.getAssetId()).keySet();
            view.setPossibleRoutes(Constants.COMMA_JOINER.join(possibleGeofenceRouteNames));

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

        VehicleState v = vehicleStateDao.getVehicleState(assetPosition.getAssetId());
        if(!passesPreliminaryChecks(assetPosition, v)) {
            return;
        }

        statsLogDao.insertAssetPosition(assetPosition);

        List<LatLng> currTrail = assetPosition.getTrail();
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

        List<String> nearbyRoutes = subrouteDao.getGpsEnabledRoutesWithinDistance(currTrail, 100);
        if(nearbyRoutes.isEmpty()
                && (v.isWithinServiceArea() || !v.getPossibleRoutes().isEmpty())) {
            v.clearPossibleRoutes();
            v.setWithinServiceArea(false);
            vehicleStateDao.updateVehicleState(v);
            return;
        }

        Set<String> oldPossibleRoutes = v.getPossibleRoutesAsSet();
        Set<String> newPossibleRoutes = new HashSet<>(nearbyRoutes);
        newPossibleRoutes.retainAll(oldPossibleRoutes);

        if(newPossibleRoutes.size() == 1) {
            v.setLastKnownDirection(newPossibleRoutes.iterator().next());
            v.updatePossibleRoutes(newPossibleRoutes);
        } else if(nearbyRoutes.size() == 1) {
            v.setLastKnownDirection(nearbyRoutes.iterator().next());
            v.updatePossibleRoutes(nearbyRoutes);
        } else if(v.getLastKnownDirection() == null
                || !newPossibleRoutes.contains(v.getLastKnownDirection())
                || newPossibleRoutes.isEmpty()) {
            v.updatePossibleRoutes(nearbyRoutes);
        } else {
            v.updatePossibleRoutes(newPossibleRoutes);
        }

        long secondsSinceMove = (assetPosition.getWhen().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - v.getLastTrailChange().getTime()) / 1000;
        if(!v.getTrail().isEmpty() && !assetPosition.getTrail().isEmpty()) {
            float distance = (float) calcDistance(assetPosition.getTrail().get(0), v.getTrail().get(0));
            v.addSpeed(distance / secondsSinceMove);
        }

        v.setWithinServiceArea(!nearbyRoutes.isEmpty());
        v.setTrail(assetPosition.getTrail());
        v.setLastTrailChange(new Date());
        vehicleStateDao.updateVehicleState(v);
    }
}
