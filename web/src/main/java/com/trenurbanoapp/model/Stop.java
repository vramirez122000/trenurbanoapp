package com.trenurbanoapp.model;

import com.trenurbanoapp.scraper.model.LatLng;

/**
 * Created by victor on 3/2/14.
 */
public class Stop {

    private int id;
    private LatLng location;
    private String amaId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getAmaId() {
        return amaId;
    }

    public void setAmaId(String amaId) {
        this.amaId = amaId;
    }
}
