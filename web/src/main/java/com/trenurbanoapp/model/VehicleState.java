package com.trenurbanoapp.model;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.trenurbanoapp.scraper.model.LatLng;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 5/13/13
 * Time: 9:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class VehicleState {

    private Integer assetId;
    private Integer lastKnownRouteGeofenceId;
    private Integer lastKnownSubrouteId;
    private Map<Integer, Boolean> possibleGeofenceRouteIds = new HashMap<>();
    private Map<Integer, Boolean> possibleSubrouteIds = new HashMap<>();
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
    private String cardinalDirection;

    public Integer getAssetId() {
        return assetId;
    }

    public void setAssetId(Integer assetId) {
        this.assetId = assetId;
    }

    public Integer getLastKnownRouteGeofenceId() {
        return lastKnownRouteGeofenceId;
    }

    public void setLastKnownRouteGeofenceId(Integer lastKnownRouteGeofenceId) {
        this.lastKnownRouteGeofenceId = lastKnownRouteGeofenceId;
    }

    public Map<Integer, Boolean> getPossibleSubrouteIds() {
        return possibleSubrouteIds;
    }

    public void setPossibleSubrouteIds(Map<Integer, Boolean> possibleSubrouteIds) {
        this.possibleSubrouteIds = possibleSubrouteIds;
    }

    public void clearPossibleSubroutes() {
        for (Integer routeId : possibleSubrouteIds.keySet()) {
            possibleSubrouteIds.put(routeId, false);
        }
    }

    public ImmutableSet<Integer> getPossibleSubroutesAsSet() {
        return ImmutableSet.copyOf(
                Maps.filterEntries(possibleSubrouteIds, input -> {
                    if(input == null) {
                        return false;
                    }
                    return input.getValue();
                }).keySet());
    }

    public Map<Integer, Boolean> getPossibleGeofenceRouteIds() {
        return possibleGeofenceRouteIds;
    }

    public void setPossibleGeofenceRouteIds(Map<Integer, Boolean> possibleGeofenceRouteIds) {
        this.possibleGeofenceRouteIds = possibleGeofenceRouteIds;
    }

    public void clearPossibleGeofenceRoutes() {
        for (Integer routeId : possibleGeofenceRouteIds.keySet()) {
            possibleGeofenceRouteIds.put(routeId, false);
        }
    }

    public ImmutableSet<Integer> getPossibleGeofenceRoutesAsSet() {
        return ImmutableSet.copyOf(
                Maps.filterEntries(possibleGeofenceRouteIds, input -> {
                    if(input == null) {
                        return false;
                    }
                    return input.getValue();
                }).keySet());
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

    public Integer getLastKnownSubrouteId() {
        return lastKnownSubrouteId;
    }

    public void setLastKnownSubrouteId(Integer lastKnownSubrouteId) {
        this.lastKnownSubrouteId = lastKnownSubrouteId;
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

    public String getCardinalDirection() {
        return cardinalDirection;
    }

    public void setCardinalDirection(String cardinalDirection) {
        this.cardinalDirection = cardinalDirection;
    }

    public void updatePossibleSubrouteIds(Iterable<Integer> newPossibleSubrouteIds) {
        clearPossibleSubroutes();
        for (Integer id : newPossibleSubrouteIds) {
            this.possibleSubrouteIds.put(id, true);
        }
    }

    public void updatePossibleGeofenceIds(Iterable<Integer> newPossibleGeofenceIds) {
        clearPossibleGeofenceRoutes();
        for (Integer id : newPossibleGeofenceIds) {
            this.possibleGeofenceRouteIds.put(id, true);
        }
    }
}
