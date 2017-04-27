package com.trenurbanoapp.dao;

import com.trenurbanoapp.model.SubrouteKey;
import com.trenurbanoapp.model.SubrouteView;
import com.trenurbanoapp.scraper.model.LatLng;

import java.util.List;
import java.util.Map;

/**
 * Created by victor on 4/23/14.
 */
public interface SubrouteDao {

    Map<SubrouteKey, SubrouteView> getGpsEnabledSubroutesWithin100Meters(LatLng currPos, LatLng prevPos, String... routeIds);

    List<Map<String, Object>> getEtas(LatLng position);

    List<String> getGpsEnabledRoutesWithinDistance(List<LatLng> line, int distanceInMeters);

    Map<String, Object> isWithinOriginOrDestination(List<LatLng> line, SubrouteKey subroute);

    List<Map<String, Object>> getNearbySubroutesWithoutScheduleOrEta(double lat, double lng);
}
