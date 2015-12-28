package com.trenurbanoapp.service.impl;

import com.trenurbanoapp.dao.*;
import com.trenurbanoapp.model.VehicleState;
import com.trenurbanoapp.scraper.model.AssetPosition;
import com.trenurbanoapp.scraper.model.LatLng;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * Created by victor on 5/17/14.
 */
public abstract class VehicleSnapshotAlgServiceBase {

    private static final Logger log = LogManager.getLogger(VehicleSnapshotAlgServiceBase.class);
    protected VehicleStateDao vehicleStateDao;
    protected VehicleDao vehicleDao;
    protected SubrouteDao subrouteDao;
    protected StatsLogDao statsLogDao;

    public void setStatsLogDao(StatsLogDao statsLogDao) {
        this.statsLogDao = statsLogDao;
    }

    public void setVehicleStateDao(VehicleStateDao vehicleStateDao) {
        this.vehicleStateDao = vehicleStateDao;
    }

    public void setVehicleDao(VehicleDao vehicleDao) {
        this.vehicleDao = vehicleDao;
    }

    public void setSubrouteDao(SubrouteDao subrouteDao) {
        this.subrouteDao = subrouteDao;
    }

    protected boolean passesPreliminaryChecks(AssetPosition assetSnapshot, VehicleState v) {
        List<LatLng> trail = assetSnapshot.getTrail();
        if (trail.isEmpty()) {
            log.warn("assetSnapshot trail was empty at {}", LocalDateTime.now());
            return false;
        }

        if (v == null) {
            VehicleState newVState = new VehicleState();
            newVState.setAssetId(assetSnapshot.getAssetId());
            newVState.setTrail(assetSnapshot.getTrail());
            newVState.setLastTrailChange(new Date());
            vehicleStateDao.insertVehicleState(newVState);
            return false;
        }

        if (Constants.trailsAreDifferent(trail, v.getTrail())) {
            if (v.getAvgSpeed() != null && v.getAvgSpeed() > 0) {
                v.addSpeed(0);
                vehicleStateDao.updateVehicleState(v);
            }
            return false;
        }

        return true;
    }

    protected static double calcBearing(LatLng first, LatLng second) {
        double latitude1 = Math.toRadians(first.getLat());
        double latitude2 = Math.toRadians(second.getLat());
        double lonDiff = Math.toRadians(second.getLng() - first.getLng());
        double y = Math.sin(lonDiff) * Math.cos(latitude2);
        double x = Math.cos(latitude1) * Math.sin(latitude2) - Math.sin(latitude1) * Math.cos(latitude2) * Math.cos(lonDiff);
        return Math.atan2(y, x);
    }

    protected double calcDistance(LatLng first, LatLng second) {
        int R = 6_378_137;
        double lat1 = Math.toRadians(first.getLat());
        double lat2 = Math.toRadians(second.getLat());
        double lonDiff = Math.toRadians(second.getLng() - first.getLng());
        double x = lonDiff * Math.cos((lat1 + lat2) / 2);
        double y = (lat2 - lat1);
        return Math.sqrt(x*x + y*y) * R;
    }


}
