package com.trenurbanoapp.service.impl;

import com.trenurbanoapp.dao.VehicleDao;
import com.trenurbanoapp.model.Vehicle;
import com.trenurbanoapp.scraper.model.Asset;
import com.trenurbanoapp.service.VehicleSnapshotService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * Created by victor on 3/20/14.
 */
public class VehicleSnapshotServiceImpl implements VehicleSnapshotService {

    private static final Logger log = LogManager.getLogger(VehicleSnapshotServiceImpl.class);


    private VehicleDao vehicleDao;
    private Cache miscCache;

    public void setCacheManager(CacheManager cacheManager) {
        miscCache = cacheManager.getCache("miscCache");
    }

    public void setVehicleDao(VehicleDao vehicleDao) {
        this.vehicleDao = vehicleDao;
    }


    @Override
    public void updateVehicle(Asset asset) {
        Vehicle v = new Vehicle();
        if(asset.getGroupId() != null) {
            v.setGroupId(Integer.valueOf(asset.getGroupId()));
        }
        v.setName(asset.getDescription());
        v.setAssetId(asset.getId());
        if(vehicleDao.vehicleExists(v.getAssetId())) {
            vehicleDao.updateVehicle(v);
        } else {
            vehicleDao.insertVehicle(v);
        }
    }

    @Override
    public void saveVehicleSnapshots(String vehicleSnapshots) {
        miscCache.put(CacheKeys.VEHICLE_SNAPSHOTS, vehicleSnapshots);
    }

}
