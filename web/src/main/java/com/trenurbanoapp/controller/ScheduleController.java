package com.trenurbanoapp.controller;

import com.trenurbanoapp.model.IdDesc;
import com.trenurbanoapp.model.StopTime;
import com.trenurbanoapp.service.ScheduleService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContext;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * User: victor
 * Date: 8/30/11
 */
@Controller
@SessionAttributes("station")
public class ScheduleController {

    private static final Logger log = LogManager.getLogger(ScheduleController.class);

    @Inject
    private ScheduleService scheduleService;

    @ModelAttribute
    public void referenceData(ModelMap model) {
        model.put("stations", scheduleService.findAllStopAreas());
    }

    @RequestMapping("/home")
    public void home(HttpServletRequest request, RequestContext ctx) {
        System.out.printf("bogus");
    }

    @RequestMapping(value = "/stopTimes")
    public ModelAndView stopTimes(@RequestParam(required = false) String station,
                                  @RequestParam(required = false) Double lat,
                                  @RequestParam(required = false) Double lng,
                                  @RequestParam(required = false) Float accuracy,
                                  @RequestParam(required = false) String time,
                                  @RequestParam(required = false) String date,
                                  ModelMap model) {

        List<StopTime> stopTimes = scheduleService.stopTimes(station, lat, lng, accuracy, time, date);
        model.put("stopTimes", stopTimes);

        if (station != null) {
            model.put("station", station);
        } else if (!stopTimes.isEmpty()) {
            model.put("station", stopTimes.get(0).getStation());
        } else {
            model.put("station", "");
        }

        return new ModelAndView("schedule_results", model);
    }

    @RequestMapping(value = "/nearestStation")
    @ResponseBody
    public String nearestStation(@RequestParam double lat,
                                 @RequestParam double lng,
                                 @RequestParam(required = false) Float accuracy,
                                 ModelMap model) {
        IdDesc station = scheduleService.nearestStation(lat, lng, accuracy);
        model.put("station", station);
        return station.getId();
    }

    @RequestMapping(value = "/nearbyRoutesWithoutSchedules")
    public String nearbyRoutesWithoutSchedules(@RequestParam double lat,
                                               @RequestParam double lng,
                                               @RequestParam(required = false) Float accuracy,
                                               ModelMap model) {
        model.put("routes", scheduleService.nearbySubroutesWithoutSchedules(lat, lng, accuracy));
        return "nearby_routes";
    }

    @RequestMapping(value = "/nearbyEtas")
    public String nearbyEtas(@RequestParam double lat,
                                               @RequestParam double lng,
                                               @RequestParam(required = false) Float accuracy,
                                               ModelMap model) {
        model.put("etas", scheduleService.nearbyEtas(lat, lng, accuracy));
        return "nearby_etas";
    }
}