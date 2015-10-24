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
    public static final int TIMEOUT = (int) TimeUnit.DAYS.toSeconds(1L);

    @Inject
    private VehicleSnapshotAlgService vehicleSnapshotAlgService;

    @Inject
    private ConfigurationDao configurationDao;

    @Inject
    private VehicleDao vehicleDao;

    private WebApiClient webApiClient;

    @Scheduled(fixedDelay = 5000L)
    public void updateVehiclePositions() {
        log.debug("begin webapi client");
        webApiClient = new WebApiClient();
        webApiClient.setAssetsHashCallback(new AssetsHashCallback() {
            @Override
            public void execute(String token, String assetsHash, WebApiRestClient restClient) {
                updateAssets(token, assetsHash, restClient);
            }
        });
        webApiClient.setAssetPositionCallback(new AssetPositionCallback() {
            @Override
            public void execute(AssetPosition position) {
                vehicleSnapshotAlgService.updateVehicleState(position);
            }
        });
        webApiClient.listen(TIMEOUT);
    }

    private void updateAssets(String token, String assetsHash, WebApiRestClient restClient) {
        Configuration conf = configurationDao.getConfiguration();
        if(conf.getAssetsHash().equals(assetsHash)) {
            return;
        }
        AssetsResponse assetsResp = restClient.assets(token);
        if(assetsResp.isError()) {
            return;
        }

        configurationDao.saveAssetsHash(assetsHash);

        for (Asset asset : assetsResp.getAssets()) {
            Vehicle v = new Vehicle();
            if(asset.getGroupId() != null) {
                v.setGroupId(Integer.valueOf(asset.getGroupId()));
            }
            if(!asset.getDescription().startsWith("200")
                    && !asset.getDescription().startsWith("201")) {
                return;
            }

            v.setName(asset.getDescription());
            v.setAssetId(asset.getId());
            if(vehicleDao.vehicleExists(v.getAssetId())) {
                vehicleDao.updateVehicle(v);
            } else {
                vehicleDao.insertVehicle(v);
            }
        }
    }


    @Override
    public void onApplicationEvent(ContextStoppedEvent event) {
        if(webApiClient != null) {
            try {
                webApiClient.stop();
            } catch(Exception e) {
                log.warn("Exception stopping webapi client");
            }
        }
    }
}
