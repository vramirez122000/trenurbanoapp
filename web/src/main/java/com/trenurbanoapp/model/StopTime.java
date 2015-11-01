package com.trenurbanoapp.model;

import java.sql.Time;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 2/4/14
 * Time: 10:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class StopTime {

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("hh:mm:ss a", Locale.US);
    private String route;
    private String routeFullName;
    private String routeGroup;
    private String stopArea;
    private String dest;
    private LocalTime stopTime;
    private String scheduleType;
    private String color;
    private String station;
    private int errorMinutes;

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getRouteFullName() {
        return routeFullName;
    }

    public void setRouteFullName(String routeFullName) {
        this.routeFullName = routeFullName;
    }

    public String getRouteGroup() {
        return routeGroup;
    }

    public void setRouteGroup(String routeGroup) {
        this.routeGroup = routeGroup;
    }
    public LocalTime getStopTime() {
        return stopTime;
    }

    public void setStopTime(LocalTime stopTime) {
        this.stopTime = stopTime;
    }

    public String getStopArea() {
        return stopArea;
    }

    public void setStopArea(String stopArea) {
        this.stopArea = stopArea;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }

    public void setStopTimeAsSqlTime(Time time) {
        this.stopTime = time.toLocalTime();
    }

    public String getStopTimeString() {
        return TIME_FORMAT.format(stopTime);
    }

    public long getStopTimeEpochMillis() {
        return Instant.from(stopTime.atDate(LocalDate.now()).atZone(ZoneId.systemDefault())).toEpochMilli();
    }

    public long getStopTimeEtaSeconds() {
        return ChronoUnit.SECONDS.between(LocalTime.now(), stopTime);
    }

    public String getStopTimeEtaString() {
        Duration duration = Duration.between(LocalTime.now(), stopTime);
        long seconds = duration.getSeconds();
        long absSeconds = Math.abs(seconds);
        StringBuilder builder = new StringBuilder();
        if(absSeconds >= 3600) {
            builder.append(absSeconds / 3600).append(" hr ");
        }
        if(absSeconds >= 60) {
            builder.append((absSeconds % 3600) / 60).append(" min ");
        }
        builder.append(absSeconds % 60).append(" s");
        String positive = builder.toString();
        return seconds < 0 ? "-" + positive : positive;
    }


    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public int getErrorMinutes() {
        return errorMinutes;
    }

    public void setErrorMinutes(int errorMinutes) {
        this.errorMinutes = errorMinutes;
    }
}
