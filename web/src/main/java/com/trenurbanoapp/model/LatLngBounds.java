package com.trenurbanoapp.model;

import com.trenurbanoapp.scraper.model.LatLng;

/**
 * Created by victor on 3/29/14.
 */
public class LatLngBounds {

    private LatLng southwest;
    private LatLng northeast;

    public LatLng getSouthwest() {
        return southwest;
    }

    public void setSouthwest(LatLng southwest) {
        this.southwest = southwest;
    }

    public LatLng getNortheast() {
        return northeast;
    }

    public void setNortheast(LatLng northeast) {
        this.northeast = northeast;
    }
}
