package com.trenurbanoapp.webapi;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Splitter;
import com.trenurbanoapp.scraper.model.Asset;
import com.trenurbanoapp.scraper.model.AssetPosition;
import com.trenurbanoapp.scraper.model.LatLng;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 6/15/14.
 */
public class WebApiRestClient2 {

    public static final Splitter TRAIL_SPLITTER = Splitter.on("*");

    private String urlbase;
    private String username;
    private String password;
    private ObjectMapper objectMapper;
    private RestTemplate restTemplate;
    private RequestCallback requestCallback;
    private AssetPositionCallback assetPositionCallback;

    public WebApiRestClient2() {
        requestCallback = request -> {
        };
    }

    public WebApiRestClient2(String urlbase, String username, String password) {
        this.urlbase = urlbase;
        this.username = username;
        this.password = password;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void setAssetPositionCallback(AssetPositionCallback assetPositionCallback) {
        this.assetPositionCallback = assetPositionCallback;
    }

    private RestTemplate getRestTemplate() {
        if (restTemplate == null) {
            restTemplate = new RestTemplate();
        }
        return restTemplate;
    }

    private ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        return objectMapper;
    }

    private RequestCallback getRequestCallback() {
        if (requestCallback == null) {
            requestCallback = request -> {
                HttpHeaders headers = request.getHeaders();
                headers.add("username", username);
                headers.add("password", password);
            };
        }
        return requestCallback;
    }

    public AssetsResponse assets() {
        RestTemplate restTemplate = getRestTemplate();
        restTemplate.getMessageConverters();
        return restTemplate.execute(urlbase + "/asset", HttpMethod.GET,
                getRequestCallback(),
                response -> {
                    if (!response.getStatusCode().is2xxSuccessful()) {
                        return new AssetsResponse(response.getStatusCode().value(), response.getStatusText());
                    }

                    List<Asset> assets = new ArrayList<>();
                    final JsonFactory jsonFactory = getObjectMapper().getFactory();
                    final JsonParser parser = jsonFactory.createParser(response.getBody());
                    JsonToken token;
                    while ((token = parser.nextToken()) != null) {
                        switch (token) {
                            case START_OBJECT:
                                JsonNode node = parser.readValueAsTree();
                                Asset asset = createAsset(node);
                                assets.add(asset);
                                break;
                        }
                    }
                    return new AssetsResponse(assets);
                });
    }

    public AssetsPositionResponse assetsPosition() {

        RestTemplate restTemplate = getRestTemplate();
        restTemplate.getMessageConverters();
        return restTemplate.execute(urlbase + "/asset/position", HttpMethod.GET,
                getRequestCallback(), response -> {
                    if (!response.getStatusCode().is2xxSuccessful()) {
                        return new AssetsPositionResponse(response.getStatusCode().value(), response.getStatusText());
                    }

                    List<AssetPosition> assets = new ArrayList<>();
                    final JsonFactory jsonFactory = getObjectMapper().getFactory();
                    final JsonParser parser = jsonFactory.createParser(response.getBody());
                    JsonToken token;
                    while ((token = parser.nextToken()) != null) {
                        switch (token) {
                            case START_OBJECT:
                                JsonNode node = parser.readValueAsTree();
                                AssetPosition position = createAssetPosition(node);
                                assets.add(position);
                                if(assetPositionCallback != null) {
                                    assetPositionCallback.execute(position);
                                }
                                break;
                        }
                    }
                    return new AssetsPositionResponse(assets);
                });
    }

    private AssetPosition createAssetPosition(JsonNode node) {
        AssetPosition assetPos = new AssetPosition();
        assetPos.setAssetId(node.path("IDASSET").numberValue().intValue());
        assetPos.setStatus(node.path("STATUS").numberValue().intValue());
        assetPos.setStatusMessage(node.path("MSG").textValue());
        assetPos.setWhen(node.path("WHEN").textValue());
        List<LatLng> trail = new ArrayList<>(3);
        for (String coordsStr : TRAIL_SPLITTER.split(node.path("TRAIL").textValue())) {
            String[] coordsArray = coordsStr.split(",");
            LatLng latLng = new LatLng(Double.valueOf(coordsArray[0]), Double.valueOf(coordsArray[1]));
            trail.add(latLng);
        }
        assetPos.setTrail(trail);
        return assetPos;
    }

    private Asset createAsset(JsonNode node) {
        Asset asset = new Asset();
        asset.setId(node.path("IDASSET").numberValue().intValue());
        asset.setDescription(node.path("DESCRIPTION").textValue());
        asset.setGroupId(String.valueOf(node.path("IDGROUP").numberValue().intValue()));
        asset.setLicensePlate(node.path("LICENSEPLATE").textValue());
        return asset;
    }

    public static void main(String[] args) {
        WebApiRestClient2 client = new WebApiRestClient2(args[0], args[1], args[2]);

        AssetsResponse assets = client.assets();
        for (Asset asset : assets.getAssets()) {
            System.out.println(asset.getDescription());
        }

        AssetsPositionResponse posResponse = client.assetsPosition();
        for (AssetPosition a : posResponse.getPositions()) {
            System.out.println(a.getTrail());
        }
    }
}
