package com.trenurbanoapp.config;

import com.trenurbanoapp.dao.*;
import com.trenurbanoapp.service.VehicleSnapshotAlgService;
import com.trenurbanoapp.service.impl.VehicleSnapshotAlgServiceBase;
import com.trenurbanoapp.service.impl.VehicleSnapshotAlgServiceRoute;
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
            injectCommonDaos(alg);
            alg.setStopDao(stopDao);
            return alg;
        } else {
            VehicleSnapshotAlgServiceRoute alg = new VehicleSnapshotAlgServiceRoute();
            injectCommonDaos(alg);
            return alg;
        }
    }

    private void injectCommonDaos(VehicleSnapshotAlgServiceBase alg) {
        alg.setSubrouteDao(subrouteDao);
        alg.setVehicleDao(vehicleDao);
        alg.setVehicleStateDao(vehicleStateDao);
        alg.setStatsLogDao(statsLogDao);
    }
}
