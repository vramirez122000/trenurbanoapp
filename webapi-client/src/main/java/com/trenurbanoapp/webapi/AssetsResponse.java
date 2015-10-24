package com.trenurbanoapp.webapi;

import com.trenurbanoapp.scraper.model.Asset;

import java.util.List;

/**
 * Created by victor on 6/15/14.
 */
public class AssetsResponse extends ResponseBase {

    private List<Asset> assets;

    public AssetsResponse(int statusCode, String statusMessage) {
        super(statusCode, statusMessage);
    }

    public AssetsResponse(List<Asset> assets) {
        super(200, "OK");
        this.assets = assets;
    }

    public List<Asset> getAssets() {
        return assets;
    }

}
