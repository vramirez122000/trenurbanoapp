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
import org.springframework.transaction.annotation.Transactional;

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
public class MapperServiceImpl implements MapperService {

    private static final Logger log = LogManager.getLogger(MapperServiceImpl.class);

    private StatsLogDao statsLogDao;
    private Cache miscCache;
    private ObjectMapper objectMapper;
    private RouteDao routeDao;
    private StopDao stopDao;
    private SubrouteDao subrouteDao;

    public void setStatsLogDao(StatsLogDao statsLogDao) {
        this.statsLogDao = statsLogDao;
    }

    public void setCacheManager(CacheManager cacheManager) {
        miscCache = cacheManager.getCache("miscCache");
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void setRouteDao(RouteDao routeDao) {
        this.routeDao = routeDao;
    }

    public void setStopDao(StopDao stopDao) {
        this.stopDao = stopDao;
    }

    public void setSubrouteDao(SubrouteDao subrouteDao) {
        this.subrouteDao = subrouteDao;
    }

    @Override
    public String getVehicleSnapshots() {
        Cache.ValueWrapper valueWrapper = miscCache.get(CacheKeys.VEHICLE_SNAPSHOTS);
        return valueWrapper == null ? null : (String) valueWrapper.get();
    }

    @Override
    public void logGeolocation(final LatLng point, final Float accuracy) {
        statsLogDao.insertClientLog(point.getLat(), point.getLng(), accuracy);
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
            f.setId(route.getName());
            f.setProperty("color", route.getColor());
            f.setProperty("dbId", route.getId());
            f.setGeometry(objectMapper.readValue(route.getGeometryAsGeoJson(), MultiLineString.class));
            return f;
        } catch (IOException e) {
            log.warn("failed parsing: ", route.getGeometryAsGeoJson());
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getDistanceAlongNearbySubroutes(LatLng latLng) {
        return subrouteDao.getDistanceAlongNearbySubroutes2(CrsConversion.convertToNad83(latLng));
    }


}
