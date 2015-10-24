package com.trenurbanoapp.service.impl;

import com.trenurbanoapp.dao.RouteDao;
import com.trenurbanoapp.dao.ScheduleDao;
import com.trenurbanoapp.dao.StatsLogDao;
import com.trenurbanoapp.model.*;
import com.trenurbanoapp.scraper.model.LatLng;
import com.trenurbanoapp.service.ScheduleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * User: victor
 * Date: 9/10/11
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Inject
    private ScheduleDao scheduleDao;

    @Inject
    private StatsLogDao statsLogDao;

    @Inject
    private RouteDao routeDao;

    @Override
    public List<StopTime> stopTimes(String station, Double lat, Double lng, Float accuracy, String timeString, String dateString) {
        List<StopTime> stopTimes = Collections.emptyList();
        if(StringUtils.isNotBlank(station)) {
            stopTimes = scheduleDao.getNextStopTimes(station, getLocalTime(timeString));
        } else if (lat != null && lng != null) {
            statsLogDao.insertClientLog(lat, lng, accuracy);
            stopTimes = scheduleDao.getNextStopTimes(lat, lng, getLocalTime(timeString));
        }
        return filter(dateString, stopTimes);
    }



    @Override
    public List<IdDesc> findAllStopAreas() {
        return scheduleDao.findAllStopAreas();
    }

    @Override
    public IdDesc nearestStation(double lat, double lng, Float accuracy) {
        statsLogDao.insertClientLog(lat, lng, accuracy);
        return scheduleDao.findNearestStopArea(lat, lng);
    }

    @Override
    public List<Route> nearbyRoutesWithoutSchedules(double lat, double lng, Float accuracy) {
        statsLogDao.insertClientLog(lat, lng, accuracy);
        return routeDao.getNearbyRoutesWithoutSchedule(lat, lng);
    }

    @Override
    public ScheduleType scheduleType(RouteGroup routeGroup) {
        return ScheduleTypeCalculator.dayType(routeGroup, LocalDate.now());
    }

    private List<StopTime> filter(String dateString, List<StopTime> stopTimes) {
        Map<String, Integer> subrouteCounts = new HashMap<>();
        return stopTimes.stream().filter((StopTime stopTime) -> {
            RouteGroup routeGroup = RouteGroup.valueOf(stopTime.getRouteGroup());
            ScheduleType validSchedType = ScheduleTypeCalculator.dayType(routeGroup, getLocalDate(dateString));
            if (ScheduleType.valueOf(stopTime.getScheduleType()) != validSchedType) {
                return false;
            }

            String subrouteKey = stopTime.getRoute() + stopTime.getDest();
            if (!subrouteCounts.containsKey(subrouteKey)) {
                subrouteCounts.put(subrouteKey, 1);
            } else if (subrouteCounts.get(subrouteKey) > 2) {
                return false;
            }

            subrouteCounts.put(subrouteKey, subrouteCounts.get(subrouteKey) + 1);
            return true;

        }).collect(Collectors.toList());
    }

    private LocalTime getLocalTime(String timeString) {
        LocalTime time;
        if(StringUtils.isNotBlank(timeString)) {
            time = LocalTime.parse(timeString, DateTimeFormatter.ISO_TIME);
        } else {
            time = LocalTime.now();
        }
        return time;
    }

    private LocalDate getLocalDate(String dateString) {
        LocalDate time;
        if(StringUtils.isNotBlank(dateString)) {
            time = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
        } else {
            time = LocalDate.now();
        }
        return time;
    }
}
