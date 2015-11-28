package com.trenurbanoapp.webapi;

import com.trenurbanoapp.scraper.model.AssetPosition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by victor on 6/25/14.
 */
public class WebApiClient {

    private static final Logger log = LogManager.getLogger(WebApiClient.class);

    private WebApiRestClient restClient;
    private WebApiSocketClient socketClient;
    private AssetPositionCallback assetPositionCallback;
    private AssetsHashCallback assetsHashCallback;

    public void setRestClient(WebApiRestClient restClient) {
        this.restClient = restClient;
    }

    public void setSocketClient(WebApiSocketClient socketClient) {
        this.socketClient = socketClient;
    }

    public void setAssetPositionCallback(AssetPositionCallback assetPositionCallback) {
        this.assetPositionCallback = assetPositionCallback;
    }

    public void setAssetsHashCallback(AssetsHashCallback assetsHashCallback) {
        this.assetsHashCallback = assetsHashCallback;
    }

    private WebApiRestClient getRestClient() {
        if (restClient == null) {
            restClient = new WebApiRestClient();
        }
        return restClient;
    }

    private WebApiSocketClient getSocketClient() {
        if (socketClient == null) {
            socketClient = new WebApiSocketClient();
        }
        return socketClient;
    }

    public void listen(int listenTimeoutInSeconds) {
        final LoginResponse login = getRestClient().login();
        if (login.isError()) {
            return;
        }

        if (assetsHashCallback != null) {
            assetsHashCallback.execute(login.getToken(), login.getAssetsHash(), restClient);
        }

        AssetsPositionResponse assetPositions = getRestClient().assetsPosition(login.getToken());
        if (assetPositions.isError()) {
            return;
        }

        for (AssetPosition assetPosition : assetPositions.getPositions()) {
            if (assetPositionCallback != null) {
                assetPositionCallback.execute(assetPosition);
            }
        }

        getSocketClient().listenSynchronously(login.getToken(), listenTimeoutInSeconds, assetPos -> {
            log.debug(assetPos.toString());
            if (assetPositionCallback != null) {
                assetPositionCallback.execute(assetPos);
            }
        });
    }

    public void stop() {
        socketClient.stop();
    }

}

