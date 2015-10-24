package com.trenurbanoapp.dao;

import com.trenurbanoapp.model.Geofence;
import com.trenurbanoapp.scraper.model.LatLng;

import java.util.List;

/**
 * Created by victor on 3/2/14.
 */
public interface GeofenceDao {

    void insertGeofence(Geofence geofence);

    Geofence getGeofenceByAvlId(String avlId);

    List<Geofence> getContainingGeofences(List<LatLng> line, Integer... subset);

    Geofence getGeofenceById(Integer id);

    List<Geofence> getContainingGeofencesByType(List<LatLng> line, Geofence.Type type);

    boolean isWithinGeofenceByType(LatLng latLng, Geofence.Type types);

    boolean isWithinGeofence(List<LatLng> line, int geofenceId);
}
