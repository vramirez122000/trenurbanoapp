package com.trenurbanoapp.dao;

import com.trenurbanoapp.model.Geofence;
import com.trenurbanoapp.scraper.model.LatLng;

import java.util.List;

/**
 * Created by victor on 3/2/14.
 */
public interface GeofenceDao {

    boolean isWithinGeofenceByType(LatLng latLng, Geofence.Type types);

}
