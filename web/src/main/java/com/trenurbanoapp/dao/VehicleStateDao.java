package com.trenurbanoapp.dao;

import com.trenurbanoapp.model.VehicleState;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 5/14/13
 * Time: 11:00 PM
 * To change this template use File | Settings | File Templates.
 */
public interface VehicleStateDao {

    VehicleState getVehicleState(int assetId);

    List<VehicleState> getMovingVehicleStatesWithinServiceArea();

    List<VehicleState> getAll();

    boolean existsVehicleState(int assetId);

    void insertVehicleState(VehicleState vehicleState);

    void updateLocationDescription(int assetId, String locationDescription);

    void updateVehicleState(VehicleState vehicleState);

    Map<Integer, Boolean> getPossibleSubroutes(int assetId);

    List<String> getPossibleSubroutesNames(int assetId);

    boolean existsPossibleSubroute(int assetId, int subrouteId);

    void insertPossibleSubroute(int assetId, int subrouteId, boolean active);

    void updatePossibleSubroute(int assetId, int subrouteId, boolean active);

    List<String> getPossibleGeofenceRoutesNames(int assetId);

    boolean existsPossibleGeofenceRoute(int assetId, int routeId);

    void insertPossibleGeofenceRoute(int assetId, int routeId, boolean active);

    void updatePossibleGeofenceRoute(int assetId, int routeId, boolean active);

    Map<Integer, Boolean> getPossibleGeofenceRoutes(int assetId);

}
