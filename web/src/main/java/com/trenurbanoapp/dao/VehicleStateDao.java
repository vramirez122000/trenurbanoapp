package com.trenurbanoapp.dao;

import com.trenurbanoapp.model.SubrouteKey;
import com.trenurbanoapp.model.VehicleState;
import com.trenurbanoapp.model.VehicleStateContainer;

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

    VehicleStateContainer getVehicleStateContainer(int assetId);

    List<VehicleState> getMovingVehicleStatesWithinServiceArea();

    Map<Integer, VehicleState> getAllAsMap();

    List<VehicleState> getAll();

    boolean existsVehicleState(int assetId);

    void insertVehicleState(VehicleState vehicleState);

    void updateLocationDescription(int assetId, String locationDescription);

    void updateVehicleState(VehicleState vehicleState);

    Map<SubrouteKey, Boolean> getPossibleSubroutes(int assetId);

    boolean existsPossibleSubroute(int assetId, SubrouteKey subroute);

    void insertPossibleSubroute(int assetId, SubrouteKey subroute, boolean active);

    void updatePossibleSubroute(int assetId, SubrouteKey subroute, boolean active);

    boolean existsPossibleRoute(int assetId, String route);

    void insertPossibleRoute(int assetId, String route, boolean active);

    void updatePossibleRoute(int assetId, String direction, boolean active);

    Map<String, Boolean> getPossibleRoutes(int assetId);

}
