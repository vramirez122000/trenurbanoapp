package com.trenurbanoapp.editor;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trenurbanoapp.dao.SubrouteDao;
import com.trenurbanoapp.model.Subroute;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.LineString;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by victor on 7/11/15.
 */

@RestController
public class Controller {

    private ObjectMapper objMapper = new ObjectMapper();

    @Inject
    private SubrouteEditorDao subrouteDao;

    @RequestMapping("/subroutes/{route}")
    public FeatureCollection subroutes(@PathVariable("route") String route) {
        List<Map<String, Object>> subroutesForEdit = subrouteDao.getSubroutesByRouteName(route);

        List<Feature> features = subroutesForEdit.stream().map((it) -> {
            Feature f = new Feature();
            try {
                f.setGeometry(objMapper.readValue((String) it.get("geojson"), LineString.class));
                f.setId(String.valueOf(it.get("gid")));
                f.getProperties().put("name", it.get("name"));
                f.getProperties().put("dest", it.get("dest"));
                f.getProperties().put("color", it.get("color"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return f;
        }).collect(Collectors.toList());
        FeatureCollection fcol = new FeatureCollection();
        fcol.setFeatures(features);
        return fcol;
    }

    @RequestMapping(value = "/updateGeoms", method = RequestMethod.POST)
    public void update(@RequestBody List<Feature> features) throws IOException {
        for (Feature feature : features) {
            if(!(feature.getGeometry() instanceof LineString)) {
                continue;
            }
            subrouteDao.updateSubroute(feature.getId(), (LineString) feature.getGeometry());
        }
    }

}
