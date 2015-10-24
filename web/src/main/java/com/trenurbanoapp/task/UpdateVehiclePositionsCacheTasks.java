package com.trenurbanoapp.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import com.trenurbanoapp.dao.SubrouteDao;
import com.trenurbanoapp.dao.VehicleDao;
import com.trenurbanoapp.dao.VehicleStateDao;
import com.trenurbanoapp.model.Subroute;
import com.trenurbanoapp.model.Vehicle;
import com.trenurbanoapp.model.VehiclePositionView;
import com.trenurbanoapp.model.VehicleState;
import com.trenurbanoapp.service.VehicleSnapshotService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by victor on 7/4/14.
 */
@Component
public class UpdateVehiclePositionsCacheTasks {

    private static final Logger log = LogManager.getLogger(UpdateVehiclePositionsCacheTasks.class);
    public static final Joiner COMMA_JOIN = Joiner.on(",");

    @Inject
    private VehicleDao vehicleDao;

    @Inject
    private VehicleStateDao vehicleStateDao;

    @Inject
    private SubrouteDao subrouteDao;

    @Inject
    private VehicleSnapshotService vehicleSnapshotService;

    @Inject
    private ObjectMapper objectMapper;

    @Scheduled(fixedRate = 5000L)
    public void updateVehiclePositionViews() {
        log.debug("begin update vehicle position views");
        List<VehicleState> vehicleStates  = vehicleStateDao.getAll();
        List<VehiclePositionView> vehiclePositionViews = new ArrayList<>(vehicleStates.size());
        for (VehicleState state : vehicleStates) {
            if(state.getAssetId() == 834) {
                log.debug("bogus");
            }
            VehiclePositionView position = new VehiclePositionView();
            position.setAssetId(state.getAssetId());
            Vehicle vehicle = vehicleDao.getVehicle(state.getAssetId());
            if(vehicle != null) {
                position.setVehicle(vehicle.getName());
            }
            position.setTrail(state.getTrail());
            position.setWithinServiceArea(state.isWithinServiceArea());
            if(state.getLastTrailChange() != null) {
                position.setPositionChange(state.getLastTrailChange().toDate().getTime());
            }
            state.setPossibleSubrouteIds(vehicleStateDao.getPossibleSubroutes(state.getAssetId()));
            ImmutableSet<Integer> possibleSubroutes = state.getPossibleSubroutesAsSet();
            position.setInRoute(possibleSubroutes.contains(state.getLastKnownSubrouteId()));

            if(state.getLastKnownSubrouteId() != null) {
                Subroute subroute = subrouteDao.getSubroute(state.getLastKnownSubrouteId());
                position.setRoute(subroute.getRouteName());
                position.setDestination(subroute.getDestination());
            }
            List<String> possibleSubrouteNames = vehicleStateDao.getPossibleSubroutesNames(state.getAssetId());
            position.setPossibleRoutes(COMMA_JOIN.join(possibleSubrouteNames));
            position.getProps().put("withinOrigin", String.valueOf(state.isWithinOrigin()));
            position.getProps().put("avgSpeed", String.valueOf(state.getAvgSpeed()));
            position.getProps().put("cardinal", String.valueOf(state.getCardinalDirection()));
            vehiclePositionViews.add(position);
        }

        Map<String, Object> data = new HashMap<>(2);
        data.put("timeStamp", System.currentTimeMillis());
        data.put("d", vehiclePositionViews);
        try {
            vehicleSnapshotService.saveVehicleSnapshots(objectMapper.writeValueAsString(data));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
