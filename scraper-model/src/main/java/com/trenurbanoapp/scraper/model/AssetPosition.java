package com.trenurbanoapp.scraper.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * User: victor
 * Date: 5/7/13
 */
public class AssetPosition {

    private Integer assetId;
    private String driverId;
    private LocalDateTime when;
    private List<LatLng> trail = new ArrayList<>(0);
    private Integer status;
    private String statusMessage;

    public Integer getAssetId() {
        return assetId;
    }

    public void setAssetId(Integer assetId) {
        this.assetId = assetId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public LocalDateTime getWhen() {
        return when;
    }

    public void setWhen(LocalDateTime when) {
        this.when = when;
    }

    public List<LatLng> getTrail() {
        return trail;
    }

    public void setTrail(List<LatLng> trail) {
        this.trail = trail;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
