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
    private String code;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getForeignId() {
        return foreignId;
    }

    public void setForeignId(String foreignId) {
        this.foreignId = foreignId;
    }
}
