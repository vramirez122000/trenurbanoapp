package com.trenurbanoapp.service.impl;

import com.trenurbanoapp.dao.GeofenceDao;
import com.trenurbanoapp.dao.VehicleDao;
import com.trenurbanoapp.dao.VehicleStateDao;
import com.trenurbanoapp.model.VehicleState;
import com.trenurbanoapp.scraper.model.AssetPosition;
import com.trenurbanoapp.scraper.model.LatLng;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.LocalDateTime;

import java.util.List;

/**
 * Created by victor on 5/17/14.
 */
public abstract class VehicleSnapshotAlgServiceBase {

    private static final Logger log = LogManager.getLogger(VehicleSnapshotAlgServiceBase.class);
    protected VehicleStateDao vehicleStateDao;
    protected GeofenceDao geofenceDao;
    protected VehicleDao vehicleDao;

    public void setVehicleStateDao(VehicleStateDao vehicleStateDao) {
        this.vehicleStateDao = vehicleStateDao;
    }

    public void setGeofenceDao(GeofenceDao geofenceDao) {
        this.geofenceDao = geofenceDao;
    }

    public void setVehicleDao(VehicleDao vehicleDao) {
        this.vehicleDao = vehicleDao;
    }

    protected boolean passesPreliminaryChecks(AssetPosition assetSnapshot, VehicleState v) {
        List<LatLng> trail = assetSnapshot.getTrail();
        if(trail.isEmpty()) {
            log.warn("assetSnapshot trail was empty at {}", LocalDateTime.now());
            return false;
        }

        if (v == null) {
            VehicleState newVState = new VehicleState();
            newVState.setAssetId(assetSnapshot.getAssetId());
            newVState.setTrail(assetSnapshot.getTrail());
            newVState.setLastTrailChange(LocalDateTime.now());
            vehicleStateDao.insertVehicleState(newVState);
            return false;
        }

        if(Constants.trailsAreDifferent(trail, v.getTrail())) {
            if(v.getAvgSpeed() != null && v.getAvgSpeed() > 0) {
                v.addSpeed(0);
                vehicleStateDao.updateVehicleState(v);
            }
            return false;
        }

        return true;
    }

    protected static double bearing(LatLng first, LatLng second){
        double latitude1 = Math.toRadians(first.getLat());
        double latitude2 = Math.toRadians(second.getLat());
        double longDiff= Math.toRadians(second.getLng() - first.getLng());
        double y=Math.sin(longDiff)*Math.cos(latitude2);
        double x=Math.cos(latitude1)*Math.sin(latitude2)-Math.sin(latitude1)*Math.cos(latitude2)*Math.cos(longDiff);
        return Math.atan2(y, x);
    }

    protected float calcSpeed(List<LatLng> currTrail, List<LatLng> prevTrail, LocalDateTime lastTrailChange) {
        if (prevTrail.isEmpty() || currTrail.isEmpty()) {
            return 0;
        }

        LatLng prevPosition = CrsConversion.convertToNad83(prevTrail.get(0));
        LatLng currPosition = CrsConversion.convertToNad83(currTrail.get(0));
        float distance = (float) Math.sqrt(Math.pow(prevPosition.getLat() - currPosition.getLat(), 2) + Math.pow(prevPosition.getLng() - currPosition.getLng(), 2));
        float timeInSeconds = new Duration(lastTrailChange.toDateTime(), DateTime.now()).getMillis() / 1000F;
        return distance / timeInSeconds;
    }

    protected static String bearingToCardinal(double x) {
        double twoPi = 2 * Math.PI ;
        if(x < 0) {
            x = x + twoPi;
        } else if (x >= twoPi) {
            x = x % twoPi;
        }
        int index = (int) Math.round((x / twoPi) * (VehicleSnapshotAlgServiceSubroute.DIRECTIONS.length - 1));
        return VehicleSnapshotAlgServiceSubroute.DIRECTIONS[index];
    }
}
