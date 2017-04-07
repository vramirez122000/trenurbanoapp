package com.trenurbanoapp.webapi;

import com.trenurbanoapp.scraper.model.AvlRoute;

import java.util.List;

/**
 * Created by victor on 03-29-17.
 */
public class AvlRouteResponse extends ResponseBase {

    private List<AvlRoute> routes;

    public AvlRouteResponse(int statusCode, String statusMessage) {
        super(statusCode, statusMessage);
    }

    public AvlRouteResponse(List<AvlRoute> assets) {
        super(200, "OK");
        this.routes = assets;
    }

    public List<AvlRoute> getRoutes() {
        return routes;
    }

}
