package com.trenurbanoapp.parser;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.trenurbanoapp.scraper.model.Asset;
import com.trenurbanoapp.scraper.parser.ReferenceDataParseListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.time.LocalDateTime;

import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneId;

/**
 * User: victor
 */
public class ReferenceDataParserJson {

    private static final Logger log = LogManager.getLogger(ReferenceDataParserJson.class);

    private JsonFactory jsonFactory = new JsonFactory();

    public void setJsonFactory(JsonFactory factory) {
        this.jsonFactory = factory;
    }

    public void parse(InputStream in, ReferenceDataParseListener... listeners) throws IOException {

        JsonParser parser = jsonFactory.createParser(in);

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
        LocalDateTime timeStamp = Instant.ofEpochMilli(parser.getLongValue()).atZone(ZoneId.systemDefault()).toLocalDateTime();
        for (ReferenceDataParseListener listener : listeners) {
            listener.parseBegin();
        }

        while (parser.nextToken() != JsonToken.END_OBJECT) {
            fieldName = parser.getCurrentName();
            // Let's move to value
            parser.nextToken();
            if (fieldName.equals("assets")) {
                while (parser.nextToken() != JsonToken.END_ARRAY) {
                    Asset snapshot = read(parser);
                    for (ReferenceDataParseListener listener : listeners) {
                        listener.assetParsed(snapshot);
                    }
                }

            } else { // ignore, or signal error?
                log.debug("Unrecognized field {}", fieldName);
            }
        }

        for (ReferenceDataParseListener listener : listeners) {
            listener.parseEnd();
        }
        parser.close();
    }

    private static Asset read(JsonParser parser) throws IOException {
        Asset result = new Asset();
        // Iterate over object fields:
        while (parser.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = parser.getCurrentName();
            // Let's move to value
            parser.nextToken();
            if (fieldName.equals("id")) {
                result.setId((parser.getIntValue()));
            } else if (fieldName.equals("groupId")) {
                result.setGroupId(parser.getText());
            } else if (fieldName.equals("description")) {
                result.setDescription(parser.getText());
            } else { // ignore, or signal error?
                log.debug("Unrecognized field assetSnapshot.{}", fieldName);
            }
        }

        return result;
    }
}