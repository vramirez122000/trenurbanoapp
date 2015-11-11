package com.trenurbanoapp.model;

import org.geojson.LineString;

/**
 * Created by victor on 4/23/14.
 */
public class Subroute {

    private int gid;
    private String routeName;
    private String destination;
    private Integer destinationId;
    private Integer nextSubrouteId;
    private Integer originId;
    private LineString geom;

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Integer getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Integer destinationId) {
        this.destinationId = destinationId;
    }

    public Integer getNextSubrouteId() {
        return nextSubrouteId;
    }

    public void setNextSubrouteId(Integer nextSubrouteId) {
        this.nextSubrouteId = nextSubrouteId;
    }

    public Integer getOriginId() {
        return originId;
    }

    public void setOriginId(Integer originId) {
        this.originId = originId;
    }

    public LineString getGeom() {
        return geom;
    }

    public void setGeom(LineString geom) {
        this.geom = geom;
    }

    @Override
    public String toString() {
        return "Subroute{" +
                "gid=" + gid +
                ", routeName='" + routeName + '\'' +
                ", destination='" + destination + '\'' +
                '}';
    }
}
