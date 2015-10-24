package com.trenurbanoapp.model;

import com.trenurbanoapp.scraper.model.LatLng;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 5/22/13
 * Time: 9:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class Geofence {

    public static enum Type {
        ROUTE,
        GARAGE,
        TERMINAL
    }

    private Integer id;
    private Integer avlId;
    private Type type;
    private String description;
    private List<LatLng> path;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAvlId() {
        return avlId;
    }

    public void setAvlId(Integer avlId) {
        this.avlId = avlId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<LatLng> getPath() {
        return path;
    }

    public void setPath(List<LatLng> path) {
        this.path = path;
    }
}
