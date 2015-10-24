package com.trenurbanoapp.dao.jdbc;

import com.trenurbanoapp.scraper.model.LatLng;
import org.postgis.LineString;
import org.postgis.PGgeometry;
import org.postgis.Point;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 5/24/13
 * Time: 10:48 PM
 * To change this template use File | Settings | File Templates.
 */
class PolyLineMapper implements RowMapper<List<LatLng>>, ReverseRowMapper<List<LatLng>> {
    private String colName;

    PolyLineMapper(String colName) {
        this.colName = colName;
    }

    @Override
    public List<LatLng> mapRow(ResultSet rs, int rowNum) throws SQLException {
        PGgeometry pggeom = (PGgeometry) rs.getObject(colName);
        LineString line = (LineString) pggeom.getGeometry();
        Point[] points = line.getPoints();
        if(points.length == 2 && points[0].equals(points[1])) {
            return Arrays.asList(new LatLng(points[0].getY(), points[1].getX()));
        }
        List<LatLng> path = new ArrayList<>(points.length);
        for (Point p  : points) {
            path.add(new LatLng(p.getY(), p.getX()));
        }
        return path;
    }

    @Override
    public Map<String, Object> reverseMap(List<LatLng> latLngs, Action action) {
        LineString lineString = Mappers.toLineString(latLngs);
        PGgeometry pggeom = new PGgeometry(lineString);
        Map<String, Object> params = new HashMap<>(1);
        params.put(colName, pggeom);
        return params;
    }


}
