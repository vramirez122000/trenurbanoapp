package com.trenurbanoapp.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trenurbanoapp.dao.RouteDao;
import com.trenurbanoapp.dao.StatsLogDao;
import com.trenurbanoapp.dao.StopDao;
import com.trenurbanoapp.dao.SubrouteDao;
import com.trenurbanoapp.model.GetStopsRequest;
import com.trenurbanoapp.model.LatLngBounds;
import com.trenurbanoapp.model.Route;
import com.trenurbanoapp.model.Stop;
import com.trenurbanoapp.scraper.model.LatLng;
import com.trenurbanoapp.service.MapperService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.geojson.*;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 2/4/14
 * Time: 9:44 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class MapperServiceImpl implements MapperService {

    private static final Logger log = LogManager.getLogger(MapperServiceImpl.class);

    @Inject
    private StatsLogDao statsLogDao;

    @Inject
    private ObjectMapper objectMapper;

    @Inject
    private RouteDao routeDao;

    @Inject
    private StopDao stopDao;

    @Inject
    private SubrouteDao subrouteDao;

    private Cache miscCache;

    @Inject
    public void setCacheManager(CacheManager cacheManager) {
        miscCache = cacheManager.getCache("miscCache");
    }


    @Override
    public String getVehicleSnapshots() {
        Cache.ValueWrapper valueWrapper = miscCache.get(CacheKeys.VEHICLE_SNAPSHOTS);
        return valueWrapper == null ? null : (String) valueWrapper.get();
    }

    @Override
    public GeometryCollection getLogEntriesAsGeoJson(LatLngBounds bounds) {

        GeometryCollection collection = new GeometryCollection();
        List<String> logEntriesAsGeoJson = statsLogDao.getLogEntriesAsGeoJson(bounds);
        for (String s : logEntriesAsGeoJson) {
            try {
                Point p = objectMapper.readValue(s, Point.class);
                collection.add(p);
            } catch (IOException e) {
                log.warn("failed parsing: {}", s);
            }
        }
        return collection;
    }


    @Override
    public FeatureCollection getRoutesGeoJson() {
        Cache.ValueWrapper valueWrapper = miscCache.get(CacheKeys.ROUTES);
        if (valueWrapper != null) {
            return (FeatureCollection) valueWrapper.get();
        }

        List<Route> allRoutes = routeDao.getAllRoutes();
        FeatureCollection collection = new FeatureCollection();
        for (Route route : allRoutes) {
            Feature routeFeature = mapToFeature(route);
            collection.add(routeFeature);
        }

        miscCache.put(CacheKeys.ROUTES, collection);
        return collection;
    }

    @Override
    public FeatureCollection getRoutesDecorationGeoJson() {
        Cache.ValueWrapper valueWrapper = miscCache.get(CacheKeys.DECORATIONS);
        if (valueWrapper != null) {
            return (FeatureCollection) valueWrapper.get();
        }

        List<Route> allRoutes = routeDao.getRouteDecorations();
        FeatureCollection collection = new FeatureCollection();
        for (Route route : allRoutes) {
            Feature routeFeature = mapToFeature(route);
            collection.add(routeFeature);
        }

        miscCache.put(CacheKeys.DECORATIONS, collection);
        return collection;
    }

    @Override
    public List<String> getNearbyRouteNames(LatLng point) {
        return routeDao.getRoutesNamesWithinDistance(point, 300);
    }

    @Override
    public Set<String> getOriginDestinationRouteNames(LatLng origin, LatLng dest) {
        return routeDao.getOriginDestinationRouteNamesWithinDistance(origin, dest, 300);
    }

    @Override
    public MultiPoint getStopsByRouteName(String routeName) {
        List<Stop> stops = stopDao.getStopsByRouteName(routeName);
        MultiPoint geoJson = new MultiPoint();
        for (Stop stop : stops) {
            geoJson.add(new LngLatAlt(stop.getLocation().getLng(), stop.getLocation().getLat()));
        }
        return geoJson;
    }

    @Override
    public FeatureCollection getStopsByRouteNames(GetStopsRequest request) {
        FeatureCollection features = new FeatureCollection();
        List<Stop> stops = stopDao.getStopsByRouteNames(request);
        for (Stop stop : stops) {
            Feature f = new Feature();
            f.setId(String.valueOf(stop.getId()));
            List<String> allRoutesByStop = routeDao.getRouteNamesByStop(stop.getId());
            f.setProperty("allRoutes", allRoutesByStop);
            f.setProperty("amaId", stop.getAmaId());

            Set<String> routeNamesByStop = new HashSet<>();
            routeNamesByStop.addAll(allRoutesByStop);
            routeNamesByStop.retainAll(request.getRouteNames());
            f.setProperty("routes", routeNamesByStop);

            f.setGeometry(new Point(stop.getLocation().getLng(), stop.getLocation().getLat()));
            features.add(f);
        }
        return features;
    }

    private Feature mapToFeature(Route route) {
        try {
            Feature f = new Feature();
            f.setId(route.getId());
            f.setProperty("code", route.getCode());
            f.setProperty("color", route.getColor());
            f.setProperty("fullName", route.getFullName());
            f.setGeometry(objectMapper.readValue(route.getGeometryAsGeoJson(), MultiLineString.class));
            return f;
        } catch (IOException e) {
            log.warn("failed parsing: ", route.getGeometryAsGeoJson());
        }
        return null;
    }

}
