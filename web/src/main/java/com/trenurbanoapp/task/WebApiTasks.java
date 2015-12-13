package com.trenurbanoapp.task;

import com.trenurbanoapp.service.VehicleSnapshotAlgService;
import com.trenurbanoapp.webapi.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Created by victor on 6/17/14.
 */
@Component
public class WebApiTasks {

    private static final Logger log = LogManager.getLogger(WebApiTasks.class);

    @Inject
    private VehicleSnapshotAlgService vehicleSnapshotAlgService;

    @Inject
    private WebApiRestClient webApiClient;

    @Value("${gps.enabled:false}")
    private boolean gpsEnabled;

    @Scheduled(fixedDelay = 3000L)
    public void updateVehiclePositions() {
        if(!gpsEnabled) {
            return;
        }
        log.debug("begin webapi client");
        webApiClient.setAssetPositionCallback(vehicleSnapshotAlgService::updateVehicleState);
        webApiClient.assetsPosition();
    }

}
