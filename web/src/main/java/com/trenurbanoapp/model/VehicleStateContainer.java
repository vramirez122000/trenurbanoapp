package com.trenurbanoapp.model;

/**
 * Created by victor on 04-26-17.
 */
public class VehicleStateContainer {

    private VehicleState state = new VehicleState();
    private Vehicle vehicle = new Vehicle();

    public VehicleStateContainer(VehicleState state, Vehicle vehicle) {
        this.state = state;
        this.vehicle = vehicle;
    }

    public VehicleState getState() {
        return state;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }
}
