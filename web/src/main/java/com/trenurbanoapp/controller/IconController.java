package com.trenurbanoapp.controller;

import com.google.common.collect.ImmutableMap;
import com.trenurbanoapp.dao.RouteDao;
import com.trenurbanoapp.model.Route;
import com.trenurbanoapp.util.Funcs;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
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

    @RequestMapping(value = "/icon/{route}.png", produces = "image/png")
    public void getIcon(@PathVariable String route, HttpServletResponse response) throws IOException, TranscoderException {

        Route r= routeDao.findById(route);
        if(r == null) {
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

        // Create a JPEG transcoder
        PNGTranscoder t = new PNGTranscoder();

        // Set the transcoding hints.
        //t.addTranscodingHint(PNGTranscoder.KEY_WIDTH, 72);

        // Create the transcoder input.
        TranscoderInput input = new TranscoderInput(new StringReader(strWriter.toString()));



        // Create the transcoder output.
        OutputStream ostream = response.getOutputStream();
        TranscoderOutput output = new TranscoderOutput(ostream);

        // Save the image.
        t.transcode(input, output);
    }
}
