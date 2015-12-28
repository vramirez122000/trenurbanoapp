package com.trenurbanoapp.dao.jdbc;

import com.trenurbanoapp.scraper.model.LatLng;
import org.postgis.LineString;
import org.postgis.PGgeometry;
import org.postgis.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 5/24/13
 * Time: 11:00 PM
 * To change this template use File | Settings | File Templates.
 */
abstract class Mappers {

    static final int WGS84_SRID = 4326;
    static final int NAD83_SRID = 32161;

    static LineString toLineString(List<LatLng> latLngs) {
        return toLineString(latLngs, WGS84_SRID);
    }

    static LineString toLineString(List<LatLng> latLngs, int srid) {
        List<Point> points = new ArrayList<>(latLngs.size() + 1);
        for (int i = 0; i < latLngs.size(); i++) {
            LatLng latLng = latLngs.get(i);
            points.add(new Point(latLng.getLng(), latLng.getLat()));
        }
        //close the ring
        if(latLngs.size() == 1) {
            points.add(points.get(0));
        }
        LineString lineString = new LineString(points.toArray(new Point[points.size()]));
        lineString.setSrid(srid);
        return lineString;
    }

    static Point toPoint(LatLng latLng) {
        return toPoint(latLng, WGS84_SRID);
    }

    static Point toPoint(LatLng latLng, int srid) {
        Point point = new Point(latLng.getLng(), latLng.getLat());
        point.setSrid(srid);
        return point;
    }

    static PGgeometry toPGGeometry(List<LatLng> lineOrPoint, int srid) {
        PGgeometry geom = null;
        if(lineOrPoint.size() > 1) {
            geom = new PGgeometry(Mappers.toLineString(lineOrPoint));
        } else if (lineOrPoint.size() == 1) {
            geom = new PGgeometry(Mappers.toPoint(lineOrPoint.get(0)));
        }
        return geom;
    }

    static PGgeometry toPGGeometry(List<LatLng> lineOrPoint) {
        return toPGGeometry(lineOrPoint, WGS84_SRID);
    }

    static PGgeometry toPGGeometry(LatLng point, int srid) {
        return new PGgeometry(Mappers.toPoint(point, srid));
    }

    static PGgeometry toPGGeometry(LatLng point) {
        return new PGgeometry(Mappers.toPoint(point, WGS84_SRID));
    }

}
