package com.trenurbanoapp.controller;

import com.trenurbanoapp.model.GetStopsRequest;
import com.trenurbanoapp.model.LatLngBounds;
import com.trenurbanoapp.scraper.model.LatLng;
import com.trenurbanoapp.service.MapperService;
import org.geojson.FeatureCollection;
import org.geojson.GeometryCollection;
import org.geojson.MultiPoint;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 2/5/14
 * Time: 10:35 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class MapperController {

    @Inject
    private MapperService mapperService;

    @RequestMapping("mapper")
    public void doIt(HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String vehicleSnapshots = mapperService.getVehicleSnapshots();

        if(vehicleSnapshots == null) {
            return;
        }

        response.getWriter().write(vehicleSnapshots);
    }

    @ResponseBody
    @RequestMapping("clientLog")
    public GeometryCollection getClientLog(@RequestBody LatLngBounds bounds) {
        return mapperService.getLogEntriesAsGeoJson(bounds);
    }

    @ResponseBody
    @RequestMapping("routes")
    public FeatureCollection getRoutes() {
        return mapperService.getRoutesGeoJson();
    }

    @ResponseBody
    @RequestMapping("stops/{routeName}")
    public MultiPoint getStopsByRoutName(@PathVariable String routeName) {
        return mapperService.getStopsByRouteName(routeName);
    }

    @ResponseBody
    @RequestMapping("stopsByRouteNames")
    public FeatureCollection getStopsByRouteNames(@RequestBody GetStopsRequest request) {
        return mapperService.getStopsByRouteNames(request);
    }

    @RequestMapping("nearbyRouteNames")
    @ResponseBody
    public List<String> getNearbyRoutes(@RequestBody LatLng latLng) {
        List<String> nearbyRouteNames = mapperService.getNearbyRouteNames(latLng);
        return nearbyRouteNames;
    }

    @RequestMapping("originDestinationRouteNames")
    @ResponseBody
    public Set<String> getOriginDestinationRouteNames(@RequestBody LatLng[] originAndDest) {
        Set<String> nearbyRouteNames = mapperService.getOriginDestinationRouteNames(originAndDest[0], originAndDest[1]);
        return nearbyRouteNames;
    }

    @RequestMapping("getSubrouteDistances")
    @ResponseBody
    public List<Map<String, Object>> getSubrouteDistances(@RequestBody LatLng latLng) {
        List<Map<String, Object>> distances = mapperService.getDistanceAlongNearbySubroutes(latLng);
        return distances;
    }

}
