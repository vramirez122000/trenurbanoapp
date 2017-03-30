package com.trenurbanoapp.scraper.model;

/**
 * Created by victor on 03-21-17.
 */
public class AvlRoute {

    private Integer id;
    private String description;
    private String routeCode;
    private String departureIntervalP1W;
    private String departureIntervalP1S;
    private String departureIntervalP1SnH;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }

    public String getDepartureIntervalP1W() {
        return departureIntervalP1W;
    }

    public void setDepartureIntervalP1W(String departureIntervalP1W) {
        this.departureIntervalP1W = departureIntervalP1W;
    }

    public String getDepartureIntervalP1S() {
        return departureIntervalP1S;
    }

    public void setDepartureIntervalP1S(String departureIntervalP1S) {
        this.departureIntervalP1S = departureIntervalP1S;
    }

    public String getDepartureIntervalP1SnH() {
        return departureIntervalP1SnH;
    }

    public void setDepartureIntervalP1SnH(String departureIntervalP1SnH) {
        this.departureIntervalP1SnH = departureIntervalP1SnH;
    }
}
