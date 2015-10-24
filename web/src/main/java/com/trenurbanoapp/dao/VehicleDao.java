package com.trenurbanoapp.dao;

import com.trenurbanoapp.model.Vehicle;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 2/16/14
 * Time: 8:58 PM
 * To change this template use File | Settings | File Templates.
 */
public interface VehicleDao {
    Vehicle getVehicle(int assetId);

    boolean vehicleExists(int assetId);

    void updateVehicle(Vehicle vehicle);

    void insertVehicle(Vehicle vehicle);
}
