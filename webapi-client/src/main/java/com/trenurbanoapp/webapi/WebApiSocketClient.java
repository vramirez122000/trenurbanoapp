package com.trenurbanoapp.webapi;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.common.base.Splitter;
import com.trenurbanoapp.scraper.model.AssetPosition;
import com.trenurbanoapp.scraper.model.LatLng;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class WebApiSocketClient {

    private static final Logger log = LogManager.getLogger(WebApiSocketClient.class);
    private static final Splitter COL_SPLIT = Splitter.on("|");
    private static final Splitter COORDS_SPLIT = Splitter.on(",");

    private String urlbase;
    private AtomicInteger countdown = new AtomicInteger();

    public WebApiSocketClient() {
    }

    public WebApiSocketClient(String urlbase) {
        this.urlbase = urlbase;
    }

    public void listenSynchronously(
            final String token,
            final int listenTimeoutInSeconds,
            final AssetPositionCallback assetPosCallback) {
        countdown.set(listenTimeoutInSeconds);
        Socket socket = null;
        try {
            socket = IO.socket(urlbase);
            socket.on(Socket.EVENT_CONNECT, new ConnectListener(socket, token));
            socket.on("update", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    log.debug("Received message: {}", args);
                    String row = (String) args[0];
                    AssetPosition assetPos = parseAssetPosition(row);
                    if (assetPos == null) return;
                    assetPosCallback.execute(assetPos);
                }

            });
            socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    log.info("socket disconnect");
                    countdown.set(0);
                }
            });
            socket.connect();
            while(countdown.getAndDecrement() > 0) {
                Thread.sleep(1000L);
            }
            socket.disconnect();

        } catch (InterruptedException e) {
            log.warn("Thread interrupted");
        } catch (Exception e) {
            log.error("Caught exception", e);
            throw new RuntimeException(e);
        } finally {
            if(socket != null) {
                try {
                    socket.close();
                } catch (Exception e) {
                    log.warn("Exception closing websocket");
                }
            }
        }
    }

    public void stop() {
        countdown.set(0);
    }

    private AssetPosition parseAssetPosition(String row) {
        List<String> cols = COL_SPLIT.splitToList(row);
        if(!"1".equals(cols.get(0))) {
            return null;
        }
        AssetPosition assetPos = new AssetPosition();
        assetPos.setAssetId(Integer.valueOf(cols.get(1)));
        assetPos.setWhen(cols.get(2));
        List<String> splittedCoords = COORDS_SPLIT.splitToList(cols.get(3));
        assetPos.setTrail(Arrays.asList(new LatLng(Double.valueOf(splittedCoords.get(0)), Double.valueOf(splittedCoords.get(1)))));
        int status;
        switch(cols.get(5)) {
            case "1":
                status = 200; //running
                break;
            case "2":
                status = 300; //idle
                break;
            case "4":
                status = 100; //stopped
                break;
            default:
                status = 300; //idle
        }
        assetPos.setStatus(status);
        return assetPos;
    }

    private static class ConnectListener implements Emitter.Listener {

        private final Socket socket;
        private final String token;

        public ConnectListener(Socket socket, String token) {
            this.socket = socket;
            this.token = token;
        }

        @Override
        public void call(Object... args) {
            socket.emit("register", token);
        }

    }

}