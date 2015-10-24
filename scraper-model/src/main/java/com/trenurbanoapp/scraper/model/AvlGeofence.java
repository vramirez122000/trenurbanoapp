package com.trenurbanoapp.scraper.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 5/8/13
 * Time: 11:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class AvlGeofence {

    public static enum Type {
        RECTANGLE("R"),
        CIRCLE("C"),
        POLYGON("S");
        private String code;

        private Type(String code) {
            this.code = code;
        }

        public static Type forCode(String s) {
            for (Type type : values()) {
                if (type.code.equals(s)) {
                    return type;
                }
            }
            return null;
        }
    }

    private Integer id;
    private String description;
    private Type type;
    private LatLng latLng1;
    private LatLng latLng2;
    private String latLngX;
    private String color;
    private List<LatLng> path = new ArrayList<>();

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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public LatLng getLatLng1() {
        return latLng1;
    }

    public void setLatLng1(LatLng latLng1) {
        this.latLng1 = latLng1;
    }

    public LatLng getLatLng2() {
        return latLng2;
    }

    public void setLatLng2(LatLng latLng2) {
        this.latLng2 = latLng2;
    }

    public String getLatLngX() {
        return latLngX;
    }

    public void setLatLngX(String latLngX) {
        this.latLngX = latLngX;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<LatLng> getPath() {
        return path;
    }

    public void setPath(List<LatLng> path) {
        if (path == null) {
            throw new IllegalArgumentException("cannot be null");
        }
        this.path = path;
    }
}