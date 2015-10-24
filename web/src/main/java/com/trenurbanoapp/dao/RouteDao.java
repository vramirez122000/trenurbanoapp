package com.trenurbanoapp.dao;

import com.trenurbanoapp.model.Route;
import com.trenurbanoapp.scraper.model.LatLng;

import java.util.List;
import java.util.Set;

/**
 * Created by victor on 3/2/14.
 */
public interface RouteDao {
    List<Route> getNearbyRoutesWithoutSchedule(double lat, double lng);

    List<Route> getAllRoutes();

    List<String> getRoutesNamesWithinDistance(LatLng point, int distanceInMeters);

    Set<String> getOriginDestinationRouteNamesWithinDistance(LatLng origin, LatLng destination, int distanceInMeters);

    List<String> getRouteNamesByStop(int stopGid);
}
