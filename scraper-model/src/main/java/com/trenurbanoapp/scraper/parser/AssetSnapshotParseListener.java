package com.trenurbanoapp.scraper.parser;

import com.trenurbanoapp.scraper.model.AssetPosition;

import java.util.Date;

/**
 * User: victor
 * Date: 5/9/13
 */
public interface AssetSnapshotParseListener {
    void parseBegin(Date timeStamp);
    void assetSnapshotParsed(AssetPosition assetSnapshot, int index);
    void parseEnd();
}
