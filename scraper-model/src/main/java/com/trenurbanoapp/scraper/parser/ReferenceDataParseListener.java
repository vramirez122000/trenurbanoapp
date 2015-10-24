package com.trenurbanoapp.scraper.parser;

import com.trenurbanoapp.scraper.model.Asset;
import com.trenurbanoapp.scraper.model.AssetGroup;
import com.trenurbanoapp.scraper.model.AvlGeofence;

/**
 * User: victor
 * Date: 5/9/13
 */
public interface ReferenceDataParseListener {

    void parseBegin();

    void assetParsed(Asset asset);

    void geofenceParsed(AvlGeofence geofence);

    void assetGroupParsed(AssetGroup group);

    void parseEnd();
}
