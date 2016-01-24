package com.trenurbanoapp.service;

import com.trenurbanoapp.model.*;

import java.util.List;
import java.util.Map;

/**
 * User: victor
 * Date: 9/9/11
 */
public interface ScheduleService {

    List<StopTime> stopTimes(String station, Double lat, Double lng, Float accuracy, String timeString, String dateString);

    List<IdDesc> findAllStopAreas();

    IdDesc nearestStation(double lat, double lng, Float accuracy);

    List<IdDesc> findStopAreasByDistance(double lat, double lng, Float accuracy);

    List<Map<String, Object>> nearbySubroutesWithoutSchedules(double lat, double lng, Float accuracy);

    List<Map<String, Object>> nearbyEtas(double lat, double lng, Float accuracy);

}
