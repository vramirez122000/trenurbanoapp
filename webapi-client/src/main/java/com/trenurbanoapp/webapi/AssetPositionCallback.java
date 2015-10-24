package com.trenurbanoapp.webapi;

import com.trenurbanoapp.scraper.model.AssetPosition;

/**
 * Created by victor on 6/24/14.
 */
public interface AssetPositionCallback {
    void execute(AssetPosition position);
}
