package com.trenurbanoapp.model;

import java.util.Set;

/**
 * Created by victor on 3/29/14.
 */
public class GetStopsRequest {

    private Set<String> routeNames;
    private LatLngBounds bounds;

    public Set<String> getRouteNames() {
        return routeNames;
    }

    public void setRouteNames(Set<String> routeNames) {
        this.routeNames = routeNames;
    }

    public LatLngBounds getBounds() {
        return bounds;
    }

    public void setBounds(LatLngBounds bounds) {
        this.bounds = bounds;
    }
}
