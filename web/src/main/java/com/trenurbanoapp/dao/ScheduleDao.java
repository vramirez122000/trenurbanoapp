package com.trenurbanoapp.dao;

import com.trenurbanoapp.model.IdDesc;
import com.trenurbanoapp.model.RouteGroup;
import com.trenurbanoapp.model.StopTime;

import java.time.LocalTime;
import java.util.List;

/**
 * Created by victor on 12/09/15.
 */
public interface ScheduleDao {
    List<StopTime> getNextStopTimes(String stopArea, LocalTime localTime);

    List<StopTime> getNextStopTimes(double lat, double lng, LocalTime localTime);

    List<IdDesc> findAllStopAreas();

    IdDesc findNearestStopArea(double lat, double lng);

    List<IdDesc> findStopAreasByDistance(double lat, double lng);
}
