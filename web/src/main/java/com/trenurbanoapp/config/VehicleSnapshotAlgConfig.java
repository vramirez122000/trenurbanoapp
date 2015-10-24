package com.trenurbanoapp.config;

import com.trenurbanoapp.dao.*;
import com.trenurbanoapp.service.VehicleSnapshotAlgService;
import com.trenurbanoapp.service.impl.VehicleSnapshotAlgServiceGeofence;
import com.trenurbanoapp.service.impl.VehicleSnapshotAlgServiceSubroute;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;

/**
 * Created by victor on 5/17/14.
 */
@Configuration
public class VehicleSnapshotAlgConfig {

    @Inject
    private GeofenceDao geofenceDao;

    @Inject
    private SubrouteDao subrouteDao;

    @Inject
    private VehicleDao vehicleDao;

    @Inject
    private VehicleStateDao vehicleStateDao;

    @Inject
    private StatsLogDao statsLogDao;

    @Inject
    private StopDao stopDao;

    @Value("${gps.routeAlgorithm.useRoutes:true}")
    private boolean useRoutes;

    @Bean
    public VehicleSnapshotAlgService vehicleSnapshotAlgService() {
        if(useRoutes) {
            VehicleSnapshotAlgServiceSubroute alg = new VehicleSnapshotAlgServiceSubroute();
            alg.setGeofenceDao(geofenceDao);
            alg.setSubrouteDao(subrouteDao);
            alg.setStatsLogDao(statsLogDao);
            alg.setVehicleDao(vehicleDao);
            alg.setVehicleStateDao(vehicleStateDao);
            alg.setStopDao(stopDao);
            return alg;
        } else {
            VehicleSnapshotAlgServiceGeofence alg = new VehicleSnapshotAlgServiceGeofence();
            alg.setVehicleStateDao(vehicleStateDao);
            alg.setVehicleDao(vehicleDao);
            alg.setGeofenceDao(geofenceDao);
            return alg;
        }
    }
}