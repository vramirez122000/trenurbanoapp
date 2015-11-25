package com.trenurbanoapp.editor;

import org.geojson.LngLatAlt;
import org.postgis.LineString;
import org.postgis.PGgeometry;
import org.postgis.Point;
import org.postgis.java2d.PGShapeGeometry;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * Created by victor on 11/11/15.
 */
@Repository
public class SubrouteEditorDao {

    private JdbcOperations jdbcTemplate;

    @Inject
    public SubrouteEditorDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Map<String, Object>> getSubroutesByRouteName(String routeName) {
        return jdbcTemplate.queryForList("SELECT subroute.gid, subroute.name, subroute.dest, route.color, " +
                " st_asgeojson(st_transform(subroute.geom, 4326)) geojson " +
                " FROM subroute JOIN route ON subroute.name = route.name " +
                " WHERE subroute.name = ? ", routeName);
    }


    public void updateSubroute(String gid, org.geojson.LineString geojson) {
        Point[] points = new Point[geojson.getCoordinates().size()];
        List<LngLatAlt> coordinates = geojson.getCoordinates();
        for (int i = 0; i < coordinates.size(); i++) {
            LngLatAlt ltlng = coordinates.get(i);
            points[i] = new Point(ltlng.getLongitude(), ltlng.getLatitude());
        }
        LineString lineStr = new LineString(points);
        int rows = jdbcTemplate.update("update subroute set geom = ST_Transform(ST_SetSRID(?, 4326), 32161) where gid = ?", new PGgeometry(lineStr), gid);
        assert rows == 1;
    }


    public List<Map<String, Object>> getStopsByRouteNames(String routeName) {
        String sql = "select stop.gid, st_asgeojson(st_transform(stop.geom, 4326)) geojson " +
                " from stop " +
                " join stop_route on stop.gid = stop_route.stop_gid " +
                " join route on stop_route.route_gid = route.gid " +
                " where route.name = ?";
        return jdbcTemplate.queryForList(sql, routeName);
    }
}
