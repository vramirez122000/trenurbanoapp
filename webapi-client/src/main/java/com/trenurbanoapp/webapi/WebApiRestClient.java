package com.trenurbanoapp.webapi;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trenurbanoapp.scraper.model.Asset;
import com.trenurbanoapp.scraper.model.AssetPosition;
import com.trenurbanoapp.scraper.model.LatLng;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 6/15/14.
 */
public class WebApiRestClient {


    private String urlbase;
    private String username;
    private String password;
    private ObjectMapper objectMapper;
    private HttpClient httpClient;
    private RequestCallback requestCallback;
    private AssetPositionCallback assetPositionCallback;

    public WebApiRestClient() {
        requestCallback = request -> {
        };
    }

    public WebApiRestClient(String urlbase, String username, String password) {
        this.urlbase = urlbase;
        this.username = username;
        this.password = password;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void setAssetPositionCallback(AssetPositionCallback assetPositionCallback) {
        this.assetPositionCallback = assetPositionCallback;
    }

    private HttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = HttpClientBuilder.create().build();
        }
        return httpClient;
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
                request.addHeader("username", username);
                request.addHeader("password", password);
            };
        }
        return requestCallback;
    }

    public AssetsResponse assets() {
        HttpClient httpClient = getHttpClient();
        HttpGet httpGet = new HttpGet(urlbase + "/asset");
        getRequestCallback().doWithRequest(httpGet);
        try {
            return httpClient.execute(httpGet, response -> {
                int status = response.getStatusLine().getStatusCode();
                if (!(status >= 200 && status < 300)) {
                    return new AssetsResponse(status, response.getStatusLine().getReasonPhrase());
                }

                List<Asset> assets = new ArrayList<>();
                final JsonFactory jsonFactory = getObjectMapper().getFactory();
                final JsonParser parser = jsonFactory.createParser(response.getEntity().getContent());
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public AssetsPositionResponse assetsPosition() {

        HttpClient httpClient = getHttpClient();
        HttpGet httpGet = new HttpGet(urlbase + "/asset/position");
        getRequestCallback().doWithRequest(httpGet);
        try {
            return httpClient.execute(httpGet, response -> {
                int status = response.getStatusLine().getStatusCode();
                if (!(status >= 200 && status < 300)) {
                    return new AssetsPositionResponse(status, response.getStatusLine().getReasonPhrase());
                }

                List<AssetPosition> assets = new ArrayList<>();
                final JsonFactory jsonFactory = getObjectMapper().getFactory();
                final JsonParser parser = jsonFactory.createParser(response.getEntity().getContent());
                JsonToken token;
                while ((token = parser.nextToken()) != null) {
                    switch (token) {
                        case START_OBJECT:
                            JsonNode node = parser.readValueAsTree();
                            AssetPosition position = createAssetPosition(node);
                            assets.add(position);
                            if (assetPositionCallback != null) {
                                assetPositionCallback.execute(position);
                            }
                            break;
                    }
                }
                return new AssetsPositionResponse(assets);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private AssetPosition createAssetPosition(JsonNode node) {
        AssetPosition assetPos = new AssetPosition();
        assetPos.setAssetId(node.path("IDASSET").numberValue().intValue());
        assetPos.setStatus(node.path("STATUS").numberValue().intValue());
        assetPos.setStatusMessage(node.path("MSG").textValue());
        assetPos.setWhen(LocalDateTime.parse(node.path("WHEN").textValue()));
        List<LatLng> trail = new ArrayList<>(3);
        String trailStr = node.path("TRAIL").textValue();

        for (String coordsStr : trailStr.split("\\*")) {
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
        asset.setGroupId(String.valueOf(node.path("IDGROUP").numberValue()));
        asset.setLicensePlate(node.path("LICENSEPLATE").textValue());
        return asset;
    }

    public static void main(String[] args) {
        WebApiRestClient client = new WebApiRestClient(args[0], args[1], args[2]);

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
