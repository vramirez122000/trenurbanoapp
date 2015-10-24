package com.trenurbanoapp.scraper.parser;

import com.trenurbanoapp.scraper.model.Asset;
import com.trenurbanoapp.scraper.model.AssetGroup;
import com.trenurbanoapp.scraper.model.AvlGeofence;

/**
 * Created by victor on 5/17/14.
 */
public abstract class AbstractReferenceDataParseListener implements ReferenceDataParseListener {
    @Override
    public void parseBegin() {

    }

    @Override
    public void assetParsed(Asset asset) {

    }

    @Override
    public void geofenceParsed(AvlGeofence geofence) {

    }

    @Override
    public void assetGroupParsed(AssetGroup group) {

    }

    @Override
    public void parseEnd() {

    }
}
