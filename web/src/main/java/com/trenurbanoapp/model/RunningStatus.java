package com.trenurbanoapp.model;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 5/27/13
 * Time: 11:58 AM
 * To change this template use File | Settings | File Templates.
 */
public enum RunningStatus {

    RUNNING(200, 299),
    IDLE(300, 399),
    STOPPED(100, 199),
    NOT_REPORTING(400, 499),
    LOW_BATTERY(500, 599),
    UNKNOWN(null, null);

    private Integer minInclusive;
    private Integer maxInclusive;

    private RunningStatus(Integer minInclusive, Integer maxInclusive) {
        this.minInclusive = minInclusive;
        this.maxInclusive = maxInclusive;
    }

    public static RunningStatus forStatusCode(Integer statusCode) {
        if(statusCode == null) {
            return UNKNOWN;
        }
        for (RunningStatus s : values()) {
            if(statusCode >= s.minInclusive && statusCode <= s.maxInclusive) {
                return s;
            }
        }
        return UNKNOWN;
    }
}
