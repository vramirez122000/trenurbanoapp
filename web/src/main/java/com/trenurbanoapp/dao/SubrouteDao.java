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

    List<Map<String, Object>> getDistanceAlongNearbySubroutes(LatLng position);

    Map<SubrouteKey, SubrouteView> getGpsEnabledSubroutesWithinDistance(List<LatLng> line, int distanceInMeters, SubrouteKey... subset);

    List<String> getGpsEnabledRoutesWithinDistance(List<LatLng> line, int distanceInMeters);

    Map<String, Object> isWithinOriginOrDestination(List<LatLng> line, SubrouteKey subroute);
}
