package com.trenurbanoapp.model;

import com.trenurbanoapp.scraper.model.AssetPosition;
import com.trenurbanoapp.scraper.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 5/25/13
 * Time: 11:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class VehiclePositionView {

    private String vehicle;
    private Integer assetId;
    private List<LatLng> trail = new ArrayList<>(0);
    private RunningStatus status;
    private Long positionChange;
    private String route;
    private boolean inRoute;
    private String possibleRoutes;
    private String destination;
    private boolean withinServiceArea;
    private Map<String,String> props = new HashMap<>();

    public VehiclePositionView() {
    }

    public VehiclePositionView(AssetPosition assetSnapshot) {
        assetId = assetSnapshot.getAssetId();
        trail = assetSnapshot.getTrail();
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public Integer getAssetId() {
        return assetId;
    }

    public void setAssetId(Integer assetId) {
        this.assetId = assetId;
    }

    public List<LatLng> getTrail() {
        return trail;
    }

    public void setTrail(List<LatLng> trail) {
        this.trail = trail;
    }

    public RunningStatus getStatus() {
        return status;
    }

    public void setStatus(RunningStatus runningStatus) {
        this.status = runningStatus;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String routeName) {
        this.route = routeName;
    }

    public boolean isInRoute() {
        return inRoute;
    }

    public void setInRoute(boolean inRoute) {
        this.inRoute = inRoute;
    }

    public String getPossibleRoutes() {
        return possibleRoutes;
    }

    public void setPossibleRoutes(String possibleRoutes) {
        this.possibleRoutes = possibleRoutes;
    }

    public Long getPositionChange() {
        return positionChange;
    }

    public void setPositionChange(Long positionChange) {
        this.positionChange = positionChange;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public boolean isWithinServiceArea() {
        return withinServiceArea;
    }

    public void setWithinServiceArea(boolean withinServiceArea) {
        this.withinServiceArea = withinServiceArea;
    }

    public Map<String, String> getProps() {
        return props;
    }

    public void setProps(Map<String, String> props) {
        this.props = props;
    }
}
