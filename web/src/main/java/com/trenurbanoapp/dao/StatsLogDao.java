package com.trenurbanoapp.dao;

import com.trenurbanoapp.model.LatLngBounds;
import com.trenurbanoapp.model.VehicleState;
import com.trenurbanoapp.scraper.model.AssetPosition;
import com.trenurbanoapp.scraper.model.LatLng;
import org.springframework.scheduling.annotation.Async;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 2/19/14
 * Time: 3:02 AM
 * To change this template use File | Settings | File Templates.
 */
public interface StatsLogDao {
    void insertClientLog(double lat, double lng, Float accuracy);

    void insertAssetPosition(AssetPosition assetPosition);

    List<String> getLogEntriesAsGeoJson(LatLngBounds bounds);

    long nextTripId();

    void insertTripLog(VehicleState vehicleState, Integer stopId, List<LatLng> trail);


}
