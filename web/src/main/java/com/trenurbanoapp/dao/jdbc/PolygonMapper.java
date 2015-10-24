package com.trenurbanoapp.dao.jdbc;

import com.trenurbanoapp.scraper.model.LatLng;
import org.postgis.LinearRing;
import org.postgis.PGgeometry;
import org.postgis.Point;
import org.postgis.Polygon;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: victor
 * Date: 5/24/13
 * Time: 10:48 PM
 */
class PolygonMapper implements RowMapper<List<LatLng>>, ReverseRowMapper<List<LatLng>> {
    private String colName;

    PolygonMapper(String colName) {
        this.colName = colName;
    }

    @Override
    public List<LatLng> mapRow(ResultSet rs, int rowNum) throws SQLException {
        PGgeometry pggeom = (PGgeometry) rs.getObject(colName);
        Polygon polygon = (Polygon) pggeom.getGeometry();
        List<LatLng> path = new ArrayList<>(polygon.getRing(0).getPoints().length);
        Point[] points = polygon.getRing(0).getPoints();
        for (int i = 0; i < points.length - 1; i++) {
            Point p = points[i];
            path.add(new LatLng(p.getY(), p.getX()));
        }
        return path;
    }

    @Override
    public Map<String, Object> reverseMap(List<LatLng> latLngs, Action action) {
        Point[] points = new Point[latLngs.size() + 1]; //+1 to close the ring
        List<LatLng> path = latLngs;
        for (int i = 0; i < path.size(); i++) {
            LatLng latLng = path.get(i);
            points[i] = new Point(latLng.getLng(), latLng.getLat());
        }
        //close the ring
        points[points.length - 1] = points[0];
        Polygon polygon = new Polygon(new LinearRing[]{new LinearRing(points)});
        polygon.setSrid(Mappers.WGS84_SRID);
        Map<String, Object> params = new HashMap<>(1);
        params.put(colName, new PGgeometry(polygon));
        return params;
    }
}
