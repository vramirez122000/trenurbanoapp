package com.trenurbanoapp.task;

import com.trenurbanoapp.dao.ConfigurationDao;
import com.trenurbanoapp.dao.VehicleDao;
import com.trenurbanoapp.model.Configuration;
import com.trenurbanoapp.model.Vehicle;
import com.trenurbanoapp.service.VehicleSnapshotAlgService;
import com.trenurbanoapp.scraper.model.Asset;
import com.trenurbanoapp.scraper.model.AssetPosition;
import com.trenurbanoapp.webapi.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

/**
 * Created by victor on 6/17/14.
 */
@Component
public class WebApiTasks implements ApplicationListener<ContextStoppedEvent> {

    private static final Logger log = LogManager.getLogger(WebApiTasks.class);

    @Inject
    private VehicleSnapshotAlgService vehicleSnapshotAlgService;

    @Inject
    private VehicleDao vehicleDao;

    private WebApiRestClient2 webApiClient;

    @Scheduled(fixedDelay = 3000L)
    public void updateVehiclePositions() {
        log.debug("begin webapi client");
        webApiClient = new WebApiRestClient2();
        webApiClient.setAssetPositionCallback(position -> vehicleSnapshotAlgService.updateVehicleState(position));
    }




    @Override
    public void onApplicationEvent(ContextStoppedEvent event) {
    }
}
