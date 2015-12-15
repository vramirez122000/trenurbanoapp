package com.trenurbanoapp.controller;

import com.google.common.collect.ImmutableMap;
import com.trenurbanoapp.dao.RouteDao;
import com.trenurbanoapp.model.Route;
import com.trenurbanoapp.util.Funcs;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.velocity.app.VelocityEngine;
import org.geojson.FeatureCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;

/**
 * Created by victor on 5/12/15.
 */
@Controller
public class IconController {

    @Autowired
    RouteDao routeDao;

    @Autowired
    VelocityEngine velocityEngine;

    @Autowired
    CacheManager cacheManager;

    @RequestMapping(value = "/icon/{imgPath}.png", produces = "image/png")
    public void getIcon(@PathVariable String imgPath, HttpServletResponse response) throws IOException, TranscoderException {

        int retinaStrIdx = imgPath.indexOf("@2x");
        String route;
        if(retinaStrIdx > 0) {
            route = imgPath.substring(0, retinaStrIdx);
        }  else {
            route = imgPath;
        }


        Cache cache = cacheManager.getCache("icons");
        Cache.ValueWrapper valueWrapper = cache.get(imgPath);
        if (valueWrapper != null) {
            byte[] img = (byte[]) valueWrapper.get();
            response.getOutputStream().write(img);
            return;
        }

        Route r= routeDao.findById(route);
        if(r == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        StringWriter strWriter = new StringWriter();
        ImmutableMap<String, Object> params = ImmutableMap.of(
                "borderLight", Funcs.shade(r.getColor(), 0.8F),
                "borderDark", Funcs.shade(r.getColor(), 0.5F),
                "fillLight", Funcs.shade(r.getColor(), 1.0F),
                "fillDark", Funcs.shade(r.getColor(), 0.7F),
                "route", route
        );
        VelocityEngineUtils.mergeTemplate(velocityEngine, "iconTemplate.svg.vm", "UTF-8", params, strWriter);

        // Create a transcoder and set the transcoding hints.
        PNGTranscoder t = new PNGTranscoder();
        if(retinaStrIdx > 0) {
            t.addTranscodingHint(PNGTranscoder.KEY_WIDTH, 41F);
            t.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, 66F);
        } else {
            t.addTranscodingHint(PNGTranscoder.KEY_WIDTH, 21F);
            t.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, 33F);
        }

        // Create the transcoder input.
        TranscoderInput input = new TranscoderInput(new StringReader(strWriter.toString()));



        // Create the transcoder output.
        ByteArrayOutputStream ostream = new ByteArrayOutputStream();
        TranscoderOutput output = new TranscoderOutput(ostream);

        // Save the image.
        t.transcode(input, output);

        byte[] img = ostream.toByteArray();
        cache.put(imgPath, img);

        response.getOutputStream().write(img);
    }

    @RequestMapping(value = "/icon/{route}.svg", produces = "image/svg+xml")
    public void getSvgIcon(@PathVariable String route, HttpServletResponse response) throws IOException, TranscoderException {

        Cache cache = cacheManager.getCache("svgIcons");
        Cache.ValueWrapper valueWrapper = cache.get(route);
        if (valueWrapper != null) {
            String svg = (String) valueWrapper.get();
            response.getOutputStream().print(svg);
            return;
        }

        Route r= routeDao.findById(route);
        if(r == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        StringWriter strWriter = new StringWriter();
        ImmutableMap<String, Object> params = ImmutableMap.of(
                "borderLight", Funcs.shade(r.getColor(), 0.8F),
                "borderDark", Funcs.shade(r.getColor(), 0.5F),
                "fillLight", Funcs.shade(r.getColor(), 1.0F),
                "fillDark", Funcs.shade(r.getColor(), 0.7F),
                "route", route
        );
        VelocityEngineUtils.mergeTemplate(velocityEngine, "iconTemplate.svg.vm", "UTF-8", params, strWriter);

        cache.put(route, strWriter.toString());
        response.getOutputStream().print(strWriter.toString());
    }
}
