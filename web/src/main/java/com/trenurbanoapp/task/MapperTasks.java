package com.trenurbanoapp.task;

import com.trenurbanoapp.parser.AssetSnapshotParserJson;
import com.trenurbanoapp.parser.ReferenceDataParserJson;
import com.trenurbanoapp.service.VehicleSnapshotAlgService;
import com.trenurbanoapp.service.VehicleSnapshotService;
import com.trenurbanoapp.scraper.model.Asset;
import com.trenurbanoapp.scraper.model.AssetPosition;
import com.trenurbanoapp.scraper.parser.AbstractAssetSnapshotParseListener;
import com.trenurbanoapp.scraper.parser.AbstractReferenceDataParseListener;
import com.trenurbanoapp.scraper.parser.AssetSnapshotParseListener;
import com.trenurbanoapp.scraper.parser.ReferenceDataParseListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.io.IOException;

/**
 * Created by victor on 3/18/14.
 */
@Component
public class MapperTasks implements BeanFactoryAware {

    public static final Logger log = LogManager.getLogger(MapperTasks.class);

    @Inject
    private VehicleSnapshotService vehicleSnapshotService;

    @Inject
    private VehicleSnapshotAlgService vehicleSnapshotAlgService;

    @Inject
    private RestTemplate restTemplate;

    @Value("${gps.url}")
    private String url;

    @Inject
    private AssetSnapshotParserJson assetSnapshotParserJson;

    @Inject
    private ReferenceDataParserJson referenceDataParserJson;

    private BeanFactory beanFactory;

    private final AssetSnapshotParseListener updateVehicleStateListener = new AbstractAssetSnapshotParseListener() {
        @Override
        public void assetSnapshotParsed(AssetPosition assetSnapshot, int index) {
            vehicleSnapshotAlgService.updateVehicleState(assetSnapshot);
        }
    };

    @Scheduled(fixedRate = 10_000, initialDelay = 2500)
    public void updateVehicleState() {
        try {

            restTemplate.execute(url + "/assetSnapshots.json", HttpMethod.GET, null, response -> {
                assetSnapshotParserJson.parse(response.getBody(), updateVehicleStateListener);
                return null;
            });

        } catch (Exception e) {
            log.error("Exception caught on vehicle snapshot processing task", e);
        }
    }

    @Scheduled(fixedRate = 5000)
    public void updateVehicleSnapshots() {
        try {

            restTemplate.execute(url + "/assetSnapshots.json", HttpMethod.GET, null, response -> {
                assetSnapshotParserJson.parse(response.getBody(), getAssetSnapshotsParseListenerVehicleSnapshots());
                return null;
            });

        } catch (Exception e) {
            log.error("Exception caught on vehicle snapshot processing task", e);
        }
    }

    @Scheduled(fixedRate = 86_400_000)
    public void updateReferenceData() {
        try {
            final ReferenceDataParseListener referenceDataParseListener = new AbstractReferenceDataParseListener() {
                @Override
                public void assetParsed(Asset asset) {
                    vehicleSnapshotService.updateVehicle(asset);
                }
            };

            restTemplate.execute(url + "/assets.json", HttpMethod.GET, null, response -> {
                referenceDataParserJson.parse(response.getBody(), referenceDataParseListener);
                return null;
            });
        } catch(Exception e) {
            log.error("Exception caught on reference data processing task", e);
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    private AssetSnapshotParseListenerVehicleSnapshots getAssetSnapshotsParseListenerVehicleSnapshots() {
        return beanFactory.getBean(AssetSnapshotParseListenerVehicleSnapshots.class);
    }
}
