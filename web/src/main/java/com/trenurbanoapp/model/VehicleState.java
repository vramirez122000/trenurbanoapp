package com.trenurbanoapp.model;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.trenurbanoapp.scraper.model.LatLng;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 5/13/13
 * Time: 9:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class VehicleState {

    private Integer assetId;
    private String lastKnownRoute;
    private String lastKnownDirection;
    private Map<String, Boolean> possibleRoutes = new HashMap<>();
    private Map<SubrouteKey, Boolean> possibleSubroutes = new HashMap<>();
    private boolean withinServiceArea;
    private List<LatLng> trail;
    private Date lastTrailChange;
    private Deque<Float> recentSpeeds = new ArrayDeque<>(10);
    private Float avgSpeed;
    private Float subrouteMeasure;
    private boolean withinOrigin;
    private Long tripId;
    private Integer stopId;
    private String locationDescription;
    private Float azimuth;

    public Integer getAssetId() {
        return assetId;
    }

    public void setAssetId(Integer assetId) {
        this.assetId = assetId;
    }


    public String getLastKnownRoute() {
        return lastKnownRoute;
    }

    public void setLastKnownRoute(String lastKnownRoute) {
        this.lastKnownRoute = lastKnownRoute;
    }

    public String getLastKnownDirection() {
        return lastKnownDirection;
    }

    public void setLastKnownDirection(String lastKnownDirection) {
        this.lastKnownDirection = lastKnownDirection;
    }



    public void clearPossibleRoutes() {
        for (String route : possibleRoutes.keySet()) {
            possibleRoutes.put(route, false);
        }
    }

    public Map<SubrouteKey, Boolean> getPossibleSubroutes() {
        return possibleSubroutes;
    }

    public void setPossibleSubroutes(Map<SubrouteKey, Boolean> possibleSubroutes) {
        this.possibleSubroutes = possibleSubroutes;
    }

    public Set<SubrouteKey> getPossibleSubroutesAsSet() {
        return possibleSubroutes.entrySet().stream()
                .filter(entry -> Boolean.TRUE.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toCollection(HashSet::new));
    }

    public void clearPossibleSubroutes() {
        for (SubrouteKey subroute : possibleSubroutes.keySet()) {
            possibleSubroutes.put(subroute, false);
        }
    }

    public Map<String, Boolean> getPossibleRoutes() {
        return possibleRoutes;
    }

    public void setPossibleRoutes(Map<String, Boolean> possibleRoutes) {
        this.possibleRoutes = possibleRoutes;
    }

    public Set<String> getPossibleRoutesAsSet() {
        return possibleRoutes.entrySet().stream()
                .filter(entry -> Boolean.TRUE.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toCollection(HashSet::new));
    }

    public List<LatLng> getTrail() {
        return trail;
    }

    public void setTrail(List<LatLng> trail) {
        this.trail = trail;
    }

    public Date getLastTrailChange() {
        return lastTrailChange;
    }

    public void setLastTrailChange(Date lastTrailChange) {
        this.lastTrailChange = lastTrailChange;
    }

    public boolean hasLastKnownRoute() {
        return lastKnownRoute != null;
    }

    public SubrouteKey getLastKnownSubroute() {
        return new SubrouteKey(lastKnownRoute, lastKnownDirection);
    }

    public void setLastKnownSubroute(SubrouteKey subroute) {
        this.lastKnownRoute = subroute.getRoute();
        this.lastKnownDirection = subroute.getDirection();
    }

    public Deque<Float> getRecentSpeeds() {
        return recentSpeeds;
    }

    public void setRecentSpeeds(Deque<Float> recentSpeeds) {
        this.recentSpeeds = recentSpeeds;
    }

    public Float getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(Float avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public boolean isWithinServiceArea() {
        return withinServiceArea;
    }

    public void setWithinServiceArea(boolean withinServiceArea) {
        this.withinServiceArea = withinServiceArea;
    }
    
    public void addSpeed(float speedInMetersPerSecond) {
        recentSpeeds.addFirst(speedInMetersPerSecond);
        if(recentSpeeds.size() > 10) {
            recentSpeeds.removeLast();
        }
        float sum = 0F;
        for (Float speed : recentSpeeds) {
            sum += speed;
        }
        avgSpeed = sum / recentSpeeds.size();
    }

    public Float getSubrouteMeasure() {
        return subrouteMeasure;
    }

    public void setSubrouteMeasure(Float subrouteMeasure) {
        this.subrouteMeasure = subrouteMeasure;
    }

    public boolean isWithinOrigin() {
        return withinOrigin;
    }

    public void setWithinOrigin(boolean withinOrigin) {
        this.withinOrigin = withinOrigin;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    public Integer getStopId() {
        return stopId;
    }

    public void setStopId(Integer stopId) {
        this.stopId = stopId;
    }

    public Float getAzimuth() {
        return azimuth;
    }

    public void setAzimuth(Float azimuth) {
        this.azimuth = azimuth;
    }

    public void updatePossibleSubroutes(Iterable<SubrouteKey> newPossibleSubroutes) {
        clearPossibleSubroutes();
        for (SubrouteKey subroute : newPossibleSubroutes) {
            this.possibleSubroutes.put(subroute, true);
        }
    }

    public void updatePossibleRoutes(Iterable<String> newPossibleRoutes) {
        clearPossibleRoutes();
        for (String route : newPossibleRoutes) {
            this.possibleRoutes.put(route, true);
        }
    }
}
