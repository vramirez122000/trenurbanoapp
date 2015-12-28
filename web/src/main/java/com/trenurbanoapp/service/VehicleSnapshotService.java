package com.trenurbanoapp.service;

import com.trenurbanoapp.model.VehicleState;

import java.util.Map;

/**
 * Created by victor on 3/20/14.
 */
public interface VehicleSnapshotService {

    Map<Integer, VehicleState> getVehicleStatesMap();

    void saveVehicleSnapshots(String vehicleSnapshots);
}
