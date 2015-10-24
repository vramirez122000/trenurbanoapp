package com.trenurbanoapp.service.impl;

import com.google.common.collect.ImmutableSet;
import com.trenurbanoapp.model.*;
import com.trenurbanoapp.scraper.model.AssetPosition;
import com.trenurbanoapp.scraper.model.LatLng;
import com.trenurbanoapp.service.VehicleSnapshotAlgService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.LocalDateTime;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by victor on 4/23/14.
 */
public class VehicleSnapshotAlgServiceGeofence extends VehicleSnapshotAlgServiceBase implements VehicleSnapshotAlgService {

    private static final Logger log = LogManager.getLogger(VehicleSnapshotAlgServiceGeofence.class);

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
                view.setPositionChange(vehicleState.getLastTrailChange().toDate().getTime());
            }
            ImmutableSet<Integer> possibleGeofenceRoutes = vehicleState.getPossibleGeofenceRoutesAsSet();
            view.setInRoute(possibleGeofenceRoutes.contains(vehicleState.getLastKnownRouteGeofenceId()));
            if(vehicleState.getLastKnownRouteGeofenceId() != null) {
                Geofence route = geofenceDao.getGeofenceById(vehicleState.getLastKnownRouteGeofenceId());
                view.setRoute(route.getDescription());
            }

            List<String> possibleGeofenceRouteNames = vehicleStateDao.getPossibleGeofenceRoutesNames(assetSnapshot.getAssetId());
            view.setPossibleRoutes(Constants.COMMA_JOINER.join(possibleGeofenceRouteNames));

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
            v.setLastTrailChange(LocalDateTime.now());
            v.setWithinServiceArea(true);
            vehicleStateDao.updateVehicleState(v);
            return;
        }

        List<Geofence> containingRouteGeofences = geofenceDao.getContainingGeofencesByType(trail, Geofence.Type.ROUTE);
        if(containingRouteGeofences.isEmpty()
                && (v.isWithinServiceArea() || !v.getPossibleGeofenceRouteIds().isEmpty())) {
            v.clearPossibleGeofenceRoutes();
            v.setWithinServiceArea(false);
            vehicleStateDao.updateVehicleState(v);
            return;
        }

        Set<Integer> containingRouteGeofenceIds = new HashSet<>();
        for (Geofence containingRouteGeofence : containingRouteGeofences) {
            containingRouteGeofenceIds.add(containingRouteGeofence.getId());
        }

        ImmutableSet<Integer> oldPossibleGeofences = v.getPossibleGeofenceRoutesAsSet();
        Set<Integer> newPossibleGeofences = new HashSet<>(containingRouteGeofenceIds);
        newPossibleGeofences.retainAll(oldPossibleGeofences);

        if(newPossibleGeofences.size() == 1) {
            v.setLastKnownRouteGeofenceId(newPossibleGeofences.iterator().next());
            v.updatePossibleGeofenceIds(newPossibleGeofences);
        } else if(containingRouteGeofenceIds.size() == 1) {
            v.setLastKnownRouteGeofenceId(containingRouteGeofenceIds.iterator().next());
            v.updatePossibleGeofenceIds(containingRouteGeofenceIds);
        } else if(v.getLastKnownRouteGeofenceId() == null
                || !newPossibleGeofences.contains(v.getLastKnownRouteGeofenceId())
                || newPossibleGeofences.isEmpty()) {
            v.updatePossibleGeofenceIds(containingRouteGeofenceIds);
        } else {
            v.updatePossibleGeofenceIds(newPossibleGeofences);
        }

        if(!v.getTrail().isEmpty() && !assetSnapshot.getTrail().isEmpty()) {
            float speedInMetersPerSecond = calcSpeed(assetSnapshot.getTrail(), v.getTrail(), v.getLastTrailChange());
            v.addSpeed(speedInMetersPerSecond);
        }

        v.setWithinServiceArea(!containingRouteGeofences.isEmpty());
        v.setTrail(assetSnapshot.getTrail());
        v.setLastTrailChange(LocalDateTime.now());
        if(assetSnapshot.getTrail().size() > 1) {
            v.setCardinalDirection(bearingToCardinal(bearing(assetSnapshot.getTrail().get(assetSnapshot.getTrail().size() - 1), assetSnapshot.getTrail().get(0))));
        }
        vehicleStateDao.updateVehicleState(v);
    }
}
