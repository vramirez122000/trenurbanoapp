package com.trenurbanoapp.dao;

import com.trenurbanoapp.model.GetStopsRequest;
import com.trenurbanoapp.model.Stop;
import com.trenurbanoapp.scraper.model.LatLng;

import java.util.List;

/**
 * Created by victor on 3/3/14.
 */
public interface StopDao {

    List<Stop> getStopsByRouteName(String routeName);

    List<Stop> getStopsByRouteNames(GetStopsRequest request);

    Stop getClosestStopWithin10Meters(List<LatLng> trail);

    Stop getStopById(int stopId);
}
