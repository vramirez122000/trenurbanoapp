package com.trenurbanoapp.service;

import com.trenurbanoapp.scraper.model.Asset;

/**
 * Created by victor on 3/20/14.
 */
public interface VehicleSnapshotService {

    void updateVehicle(Asset asset);

    void saveVehicleSnapshots(String vehicleSnapshots);
}
