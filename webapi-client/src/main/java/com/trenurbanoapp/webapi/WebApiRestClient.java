package com.trenurbanoapp.webapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.trenurbanoapp.scraper.model.Asset;
import com.trenurbanoapp.scraper.model.AssetPosition;
import com.trenurbanoapp.scraper.model.LatLng;
import org.apache.commons.codec.binary.Hex;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by victor on 6/15/14.
 */
public class WebApiRestClient {

    public static final Splitter TRAIL_SPLITTER = Splitter.on("*");

    private String urlbase;
    private String username;
    private String password;
    private ObjectMapper objectMapper;
    private RestTemplate restTemplate;

    public WebApiRestClient() {
    }

    public WebApiRestClient(String urlbase, String username, String password) {
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

    private RestTemplate getRestTemplate() {
        if(restTemplate == null) {
            restTemplate = new RestTemplate();
        }
        return restTemplate;
    }

    private ObjectMapper getObjectMapper() {
        if(objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        return objectMapper;
    }

    public LoginResponse login() {
        HttpHeaders headers = new HttpHeaders();
        headers.put("username", Arrays.asList(username));
        headers.put("password", Arrays.asList(hash(password)));
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = getRestTemplate().exchange(urlbase + "/login", HttpMethod.GET, entity, String.class);
        if(!responseEntity.getStatusCode().is2xxSuccessful()) {
            return new LoginResponse(responseEntity.getStatusCode().value(), responseEntity.getBody());
        }
        JsonNode root = getJsonNode(responseEntity.getBody());
        String token = root.path("token").textValue();
        JsonNode assetsHashJson = root.path("assetsHash");
        byte[] bytes = new byte[assetsHashJson.size()];
        int i = 0;
        for (Iterator<JsonNode> iterator = assetsHashJson.iterator(); iterator.hasNext(); i++) {
            JsonNode elem = iterator.next();
            bytes[i] = (byte) elem.intValue();
        }
        return new LoginResponse(token, byteToHex(bytes));
    }

    public AssetsResponse assets(String token) {
        ResponseEntity<String> responseEntity = getRestTemplate().exchange(urlbase + "/assets", HttpMethod.GET, createRequestEntity(token), String.class);
        if(!responseEntity.getStatusCode().is2xxSuccessful()) {
            return new AssetsResponse(responseEntity.getStatusCode().value(), responseEntity.getBody());
        }
        JsonNode root = getJsonNode(responseEntity.getBody());
        List<Asset> assets = Lists.newArrayList();
        for (JsonNode node : root) {
            Asset asset = new Asset();
            asset.setId(node.path("IDASSET").numberValue().intValue());
            asset.setGroupId(node.path("IDGROUP").textValue());
            asset.setDescription(node.path("DESCRIPTION").textValue());
            asset.setLicensePlate(node.path("LICENSEPLATE").textValue());
            assets.add(asset);
        }
        return new AssetsResponse(assets);
    }

    public AssetsPositionResponse assetsPosition(String token) {
        ResponseEntity<String> responseEntity = getRestTemplate().exchange(urlbase + "/assetsPosition", HttpMethod.GET, createRequestEntity(token), String.class);
        if(!responseEntity.getStatusCode().is2xxSuccessful()) {
            return new AssetsPositionResponse(responseEntity.getStatusCode().value(), responseEntity.getBody());
        }
        JsonNode root = getJsonNode(responseEntity.getBody()).path(0);
        List<AssetPosition> assets = Lists.newArrayList();
        for (JsonNode node : root) {
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
            assets.add(assetPos);
        }
        return new AssetsPositionResponse(assets);
    }

    private JsonNode getJsonNode(String body) {
        try {
            return getObjectMapper().readValue(body, JsonNode.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static HttpEntity<String> createRequestEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.put("token", Arrays.asList(token));
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return new HttpEntity<>(headers);
    }

    private static String hash(String input) {

        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("No instance of MD5 digest available");
        }
        messageDigest.update(input.getBytes(), 0, input.length());
        return byteToHex(messageDigest.digest());
    }

    private static String byteToHex(byte[] bytes) {
        return new String(Hex.encodeHex(bytes));
    }

}
