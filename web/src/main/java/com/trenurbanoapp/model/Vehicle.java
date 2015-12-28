package com.trenurbanoapp.model;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 6/30/13
 * Time: 5:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class Vehicle {

    private Integer assetId;
    private String name;
    private Integer groupId;


    public Integer getAssetId() {
        return assetId;
    }

    public void setAssetId(Integer assetId) {
        this.assetId = assetId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
}
