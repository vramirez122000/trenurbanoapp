package com.trenurbanoapp.task;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Joiner;
import com.trenurbanoapp.dao.VehicleStateDao;
import com.trenurbanoapp.model.VehicleState;
import com.trenurbanoapp.scraper.model.LatLng;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestOperations;
import org.springframework.web.util.UriTemplate;

import javax.inject.Inject;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by victor on 5/26/14.
 */
//@Component
public class GeocodeTasks {
    private static final Logger log = LogManager.getLogger(GeocodeTasks.class);


    private static final Joiner JOINER = Joiner.on(", ").skipNulls();
    @Inject
    private VehicleStateDao vehicleStateDao;

    @Value("Fmjtd|luur2d0zl1,82=o5-9a825r")
    private String mapquestApiKey;

    @Inject
    private AsyncRestOperations restTemplate;

    @Scheduled(fixedRate = 10_000)
    public void reverseGeocode() {

        List<VehicleState> vehicleStates = vehicleStateDao.getMovingVehicleStatesWithinServiceArea();
        for (final VehicleState v : vehicleStates) {

            if(v.getTrail() == null || v.getTrail().isEmpty()) {
                return;
            }
            LatLng location = v.getTrail().get(0);
            final Map<String, Object> params = new HashMap<>(3);
            params.put("key", mapquestApiKey);
            params.put("lat", location.getLat());
            params.put("lng", location.getLng());
            final String urlTemplate = "http://open.mapquestapi.com/geocoding/v1/reverse?key={key}&location={lat},{lng}";
            ListenableFuture<ResponseEntity<JsonNode>> future = restTemplate.getForEntity(
                    urlTemplate, JsonNode.class, params);
            future.addCallback(new ListenableFutureCallback<ResponseEntity<JsonNode>>() {
                @Override
                public void onSuccess(ResponseEntity<JsonNode> mapResponseEntity) {

                    try {
                        JsonNode root = mapResponseEntity.getBody();
                        JsonNode info = root.path("info");
                        if(new BigInteger("403").equals(info.path("statuscode").bigIntegerValue())) {
                            log.warn("Invalid mapquest api key for request {}", new UriTemplate(urlTemplate).expand(params));
                            if(!StringUtils.isEmpty(v.getLocationDescription())) {
                                vehicleStateDao.updateLocationDescription(v.getAssetId(), null);
                            }
                            return;
                        }
                        if(!BigInteger.ZERO.equals(info.path("statuscode").bigIntegerValue())) {
                            log.warn("Mapquest error for request {}", new UriTemplate(urlTemplate).expand(params));
                            log.warn("Mapquest error message {}", info.path("messages").path(0));
                            if(!StringUtils.isEmpty(v.getLocationDescription())) {
                                vehicleStateDao.updateLocationDescription(v.getAssetId(), null);
                            }
                            return;
                        }
                        JsonNode addr = root.path("results").path(0).path("locations").path(0);
                        String street = addr.path("street").textValue();
                        String municipio = addr.path("adminArea5").textValue();
                        String locationDescription = JOINER.join(street, municipio);
                        if(!locationDescription.equals(v.getLocationDescription())) {
                            vehicleStateDao.updateLocationDescription(v.getAssetId(), locationDescription);
                        }
                    } catch (Exception e) {
                        log.error("Error executing geocode tasks", e);
                        if(!StringUtils.isEmpty(v.getLocationDescription())) {
                            vehicleStateDao.updateLocationDescription(v.getAssetId(), null);
                        }
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {

                }
            });


        }

    }
}
