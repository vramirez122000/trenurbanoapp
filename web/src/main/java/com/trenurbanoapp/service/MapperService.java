package com.trenurbanoapp.service;

import com.trenurbanoapp.model.GetStopsRequest;
import com.trenurbanoapp.model.LatLngBounds;
import com.trenurbanoapp.scraper.model.LatLng;
import org.geojson.FeatureCollection;
import org.geojson.GeometryCollection;
import org.geojson.MultiPoint;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 2/4/14
 * Time: 10:00 PM
 * To change this template use File | Settings | File Templates.
 */
public interface MapperService {

    String getVehicleSnapshots();

    GeometryCollection getLogEntriesAsGeoJson(LatLngBounds bounds);

    FeatureCollection getRoutesGeoJson();

    FeatureCollection getRoutesDecorationGeoJson();

    List<String> getNearbyRouteNames(LatLng point);

    Set<String> getOriginDestinationRouteNames(LatLng origin, LatLng dest);

    MultiPoint getStopsByRouteName(String routeName);

    FeatureCollection getStopsByRouteNames(GetStopsRequest request);

}
