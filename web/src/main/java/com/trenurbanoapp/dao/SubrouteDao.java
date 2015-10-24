package com.trenurbanoapp.dao;

import com.trenurbanoapp.model.Subroute;
import com.trenurbanoapp.model.SubrouteView;
import com.trenurbanoapp.scraper.model.LatLng;

import java.util.List;
import java.util.Map;

/**
 * Created by victor on 4/23/14.
 */
public interface SubrouteDao {

    List<Map<String, Object>> getDistanceAlongNearbySubroutes2(LatLng position);

    Map<Integer, SubrouteView> getGpsEnabledSubroutesWithinDistance(List<LatLng> line, int distanceInMeters, Integer... subset);

    float getMeasure(int subrouteId, LatLng point);

    Subroute getSubroute(int id);

    Map<String, Object> isWithinOriginOrDestination(List<LatLng> line, int subrouteGid);
}
