package com.trenurbanoapp.webapi;

import com.trenurbanoapp.scraper.model.AssetPosition;

import java.util.List;

/**
 * Created by victor on 6/15/14.
 */
public class AssetsPositionResponse extends ResponseBase {

    private List<AssetPosition> positions;

    public AssetsPositionResponse(int statusCode, String statusMessage) {
        super(statusCode, statusMessage);
    }

    public AssetsPositionResponse(List<AssetPosition> snapshots) {
        super(200, "OK");
        this.positions = snapshots;
    }

    public List<AssetPosition> getPositions() {
        return positions;
    }
}
