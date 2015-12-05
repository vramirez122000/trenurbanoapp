package com.trenurbanoapp.service.impl;

import com.trenurbanoapp.model.*;
import com.trenurbanoapp.scraper.model.AssetPosition;
import com.trenurbanoapp.scraper.model.LatLng;
import com.trenurbanoapp.service.VehicleSnapshotAlgService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by victor on 4/23/14.
 */
public class VehicleSnapshotAlgServiceRoute extends VehicleSnapshotAlgServiceBase implements VehicleSnapshotAlgService {

    private static final Logger log = LogManager.getLogger(VehicleSnapshotAlgServiceRoute.class);

    @Override
    public VehiclePositionView getVehicleSnapshotView(AssetPosition assetSnapshot) {
        VehiclePositionView view = new VehiclePositionView();
        view.setAssetId(assetSnapshot.getAssetId());
        view.setTrail(assetSnapshot.getTrail());
        view.setStatus(RunningStatus.forStatusCode(assetSnapshot.getStatus()));
        VehicleState vehicleState = vehicleStateDao.getVehicleState(assetSnapshot.getAssetId());
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

            Set<String> possibleGeofenceRouteNames = vehicleStateDao.getPossibleRoutes(assetSnapshot.getAssetId()).keySet();
            view.setPossibleRoutes(Constants.COMMA_JOINER.join(possibleGeofenceRouteNames));

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

        List<LatLng> trail = assetSnapshot.getTrail();
        boolean withinTerminal = geofenceDao.isWithinGeofenceByType(trail.get(0), Geofence.Type.TERMINAL);
        if(withinTerminal) {
            v.getRecentSpeeds().clear();
            v.setAvgSpeed(0F);
            v.setTrail(trail);
            v.setLastTrailChange(new Date());
            v.setWithinServiceArea(true);
            vehicleStateDao.updateVehicleState(v);
            return;
        }

        List<String> nearbyRoutes = subrouteDao.getGpsEnabledRoutesWithinDistance(trail, 100);
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

        if(!v.getTrail().isEmpty() && !assetSnapshot.getTrail().isEmpty()) {
            float speedInMetersPerSecond = calcSpeed(assetSnapshot.getTrail(), v.getTrail(), v.getLastTrailChange());
            v.addSpeed(speedInMetersPerSecond);
        }

        v.setWithinServiceArea(!nearbyRoutes.isEmpty());
        v.setTrail(assetSnapshot.getTrail());
        v.setLastTrailChange(new Date());
        vehicleStateDao.updateVehicleState(v);
    }
}
