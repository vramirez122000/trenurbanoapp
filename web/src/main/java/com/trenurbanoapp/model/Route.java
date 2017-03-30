package com.trenurbanoapp.model;

/**
 * Created by victor on 3/3/14.
 */
public class Route {

    private String id;
    private String geometryAsGeoJson;
    private String color;
    private String name;
    private String fullName;
    private String foreignId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGeometryAsGeoJson() {
        return geometryAsGeoJson;
    }

    public void setGeometryAsGeoJson(String geometryAsGeoJson) {
        this.geometryAsGeoJson = geometryAsGeoJson;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getForeignId() {
        return foreignId;
    }

    public void setForeignId(String foreignId) {
        this.foreignId = foreignId;
    }
}
