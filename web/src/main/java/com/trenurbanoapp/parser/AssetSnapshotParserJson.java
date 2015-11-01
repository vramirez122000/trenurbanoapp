package com.trenurbanoapp.parser;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.trenurbanoapp.scraper.model.AssetPosition;
import com.trenurbanoapp.scraper.model.LatLng;
import com.trenurbanoapp.scraper.parser.AssetSnapshotParseListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * User: victor
 */
public class AssetSnapshotParserJson {

    private static final Logger log = LogManager.getLogger(AssetSnapshotParserJson.class);
    private JsonFactory jsonFactory;

    public void setJsonFactory(JsonFactory jsonFactory) {
        this.jsonFactory = jsonFactory;
    }

    public void parse(InputStream inputStream, AssetSnapshotParseListener... listeners) throws IOException {

        JsonParser parser = jsonFactory.createParser(inputStream);

        // Sanity check: verify that we got "Json Object":
        if (parser.nextToken() != JsonToken.START_OBJECT) {
            throw new IOException("Expected data to start Object, instead: " + parser.getCurrentToken());
        }

        parser.nextToken();
        String fieldName = parser.getCurrentName();
        // Let's move to value
        //assert token is timestamp
        if (!fieldName.equals("timeStamp")) {
            throw new RuntimeException("timeStamp field expected");
        }
        parser.nextToken();
        Date timeStamp = new Date(parser.getLongValue());
        for (AssetSnapshotParseListener listener : listeners) {
            listener.parseBegin(timeStamp);
        }

        while (parser.nextToken() != JsonToken.END_OBJECT) {
            fieldName = parser.getCurrentName();
            // Let's move to value
            parser.nextToken();
            if (fieldName.equals("assetSnapshots")) {
                int assetSnapshotIndex = 0;
                while (parser.nextToken() != JsonToken.END_ARRAY) {
                    AssetPosition snapshot = read(parser);
                    for (AssetSnapshotParseListener listener : listeners) {
                        listener.assetSnapshotParsed(snapshot, assetSnapshotIndex++);
                    }
                }

            } else { // ignore, or signal error?
                log.debug("Unrecognized field {}", fieldName);
            }
        }

        for (AssetSnapshotParseListener listener : listeners) {
            listener.parseEnd();
        }
        parser.close();
    }

    private static AssetPosition read(JsonParser parser) throws IOException {
        AssetPosition result = new AssetPosition();
        // Iterate over object fields:
        while (parser.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = parser.getCurrentName();
            // Let's move to value
            parser.nextToken();
            switch (fieldName) {
                case "assetId":
                    result.setAssetId((parser.getIntValue()));
                    break;
                case "status":
                    result.setStatus(parser.getValueAsInt(0));
                    break;
                case "trail":

                    // Sanity check: verify that we got "Json Object":
                    while (parser.nextToken() != JsonToken.END_ARRAY) {

                        LatLng latLng = new LatLng();
                        while (parser.nextToken() != JsonToken.END_OBJECT) {
                            String latLngFieldName = parser.getCurrentName();
                            // Let's move to value
                            parser.nextToken();
                            if (latLngFieldName.equals("lat")) {
                                latLng.setLat((parser.getDoubleValue()));
                            } else if (latLngFieldName.equals("lng")) {
                                latLng.setLng((parser.getDoubleValue()));
                            } else { // ignore, or signal error?
                                log.debug("Unrecognized field assetSnapshot.latLng.%s\n", latLngFieldName);
                            }
                        }
                        result.getTrail().add(latLng);
                    }
                    break;
                default:  // ignore, or signal error?
                    log.debug("Unrecognized field assetSnapshot.{}\n", fieldName);
                    break;
            }
        }

        return result;
    }
}