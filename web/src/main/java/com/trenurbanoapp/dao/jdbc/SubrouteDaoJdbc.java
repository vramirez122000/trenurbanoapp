package com.trenurbanoapp.dao.jdbc;

import com.trenurbanoapp.dao.SubrouteDao;
import com.trenurbanoapp.model.SubrouteKey;
import com.trenurbanoapp.model.SubrouteView;
import com.trenurbanoapp.scraper.model.LatLng;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.postgis.PGgeometry;
import org.postgis.Point;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by victor on 4/23/14.
 */
public class SubrouteDaoJdbc implements SubrouteDao {

    private static final Logger log = LogManager.getLogger(SubrouteDaoJdbc.class);

    private JdbcOperations jdbcTemplate;
    private RowMapper<SubrouteKey> rowMapper = (rs, i) -> {
        SubrouteKey s = new SubrouteKey();
        s.setRoute(rs.getString("route"));
        s.setDirection(rs.getString("direction"));
        return s;
    };

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Map<String, Object>> getDistanceAlongNearbySubroutes(LatLng position) {
        String sql = "with position as ( " +
                "  SELECT ST_Transform(?, ?) point " +
                "), " +
                "subroute_intersection_view as ( " +
                "  /* Select segments of subroute that intersect with a 300m buffer around the position */ " +
                "  SELECT " +
                "   cast(subroute.gid as int4) gid, " +
                "   subroute.name, " +
                "   subroute.dest, " +
                "   subroute.geom_with_m, " +
                "   route.color route_color, " +
                "   ST_Dump ( ST_Intersection ( subroute.geom, ST_Buffer(position.point, 300) ) ) geom_dump " +
                "  FROM subroute_new as subroute " +
                "   JOIN route_new as route ON route.gpsenabled = true and subroute.route = route.id " +
                "   JOIN position ON ST_DWithin(subroute.geom, position.point, 300) " +
                "), " +
                "vehicle_distances as ( " +
                " SELECT " +
                "  subroute_intersection_view.gid, " +
                "  subroute_intersection_view.name subroute, " +
                "  subroute_intersection_view.dest, " +
                "  cast(ST_InterpolatePoint(" +
                "       subroute_intersection_view.geom_with_m, " +
                "       ST_Line_Interpolate_Point((subroute_intersection_view.geom_dump).geom, 0.5)" +
                "     ) - vehicle_state.subroute_m AS INT) distance, " +
                "  vehicle.name vehicle_name, " +
                "  vehicle.asset_id, " +
                "  vehicle_state.avg_speed, " +
                "  vehicle_state.location_desc, " +
                "  vehicle_state.cardinal_dir, " +
                "  vehicle_state.stop_gid, " +
                "  route_color, " +
                "  lpad(cast(stop.ama_id as varchar), 4, '0') stop_ama_id, " +
                "  stop.descriptio " +
                " FROM subroute_intersection_view " +
                "  JOIN vehicle_state ON subroute_intersection_view.gid = vehicle_state.last_known_subroute_id " +
                "  JOIN vehicle ON vehicle_state.asset_id = vehicle.asset_id " +
                "  LEFT JOIN stop ON vehicle_state.stop_gid = stop.gid " +
                " where vehicle_state.subroute_m IS NOT NULL " +
                ") " +
                "select * from vehicle_distances " +
                "where distance > 0; ";

        PGgeometry geom = new PGgeometry(Mappers.toPoint(position, Mappers.WGS84_SRID));
        return jdbcTemplate.queryForList(sql, geom, Mappers.NAD83_SRID);
    }

    @Override
    public List<String> getGpsEnabledRoutesWithinDistance(List<LatLng> line, int distanceInMeters) {
        PGgeometry geom = new PGgeometry(Mappers.toLineString(line, Mappers.WGS84_SRID));
        return jdbcTemplate.query("select route from subroute_new as subroute where ST_DWithin(ST_Transform(?, 32161), ?)",
                new SingleColumnRowMapper<>(String.class), geom, distanceInMeters);
    }

    @Override
    public Map<SubrouteKey, SubrouteView> getGpsEnabledSubroutesWithinDistance(List<LatLng> line, int distanceInMeters, SubrouteKey... subset) {

        if (line.size() == 1) {
            throw new IllegalArgumentException("linestring must have more than 1 point");
        }

        PGgeometry geom = new PGgeometry(Mappers.toLineString(line, Mappers.WGS84_SRID));
        String sql = "with vehicle_trail as ( " +
                "  SELECT ST_Transform(ST_Reverse(?), 32161) AS trail " +
                "), subroute_intersection_view as ( " +
                " SELECT " +
                "  subroute.route, " +
                "  subroute.direction, " +
                "  vehicle_trail.trail, " +
                "  subroute.geom, " +
                "  ST_Dump ( " +
                "    ST_Intersection ( subroute.geom, ST_Buffer(vehicle_trail.trail, 30, 'endcap=flat') ) " +
                "  ) geom_dump " +
                " FROM subroute_new as subroute " +
                "  JOIN route_new as route ON route.gpsenabled = true and subroute.route = route.id " +
                "  JOIN vehicle_trail on ST_DWithin(subroute.geom, vehicle_trail.trail, 30) " +
                "), segment_azimuths as ( " +
                " SELECT " +
                "   route, " +
                "   direction, " +
                "   ST_PointN((geom_dump).geom, 1) subroute_position, " +
                "   ST_Length(trail) trail_length, " +
                "   ST_Length((geom_dump).geom) subroute_length, " +
                "   ST_Azimuth(ST_Startpoint(trail), st_endpoint(trail)) trail_azimuth, " +
                "   ST_Azimuth(ST_Startpoint((geom_dump).geom), st_endpoint((geom_dump).geom)) subroute_azimuth, " +
                "   ST_InterpolatePoint(geom, ST_PointN((geom_dump).geom, 1)) subroute_m " +
                " FROM subroute_intersection_view " +
                ") " +
                "SELECT route, direction, subroute_position, " +
                " trail_azimuth, subroute_azimuth, trail_length, subroute_length, subroute_m " +
                " FROM segment_azimuths " +
                " WHERE least ( " +
                "   2 * pi() - abs(trail_azimuth - subroute_azimuth), " +
                "   abs(trail_azimuth - subroute_azimuth) " +
                " ) < (0.4 * pi()) " +
                " AND abs(trail_length - subroute_length) < trail_length * 0.3 ";
        Map<SubrouteKey, SubrouteView> resultMap = jdbcTemplate.query(sql, (ResultSet rs) -> {
            Map<SubrouteKey, SubrouteView> results = new HashMap<>();
            for (int i = 0; rs.next(); i++) {
                SubrouteView v = new SubrouteView();
                SubrouteKey s = rowMapper.mapRow(rs, i);
                v.setSubroute(s);
                v.setSubrouteSegmentAzimuth(rs.getDouble("subroute_azimuth"));
                v.setVehicleTrailAzimuth(rs.getDouble("trail_azimuth"));
                v.setSubrouteSegmentLength(rs.getDouble("subroute_length"));
                v.setVehicleTrailLength(rs.getDouble("trail_length"));
                v.setSubrouteM(rs.getDouble("subroute_m"));
                PGgeometry geom1 = (PGgeometry) rs.getObject("subroute_position");
                Point point = (Point) geom1.getGeometry();
                v.setSubroutePosition(new LatLng(point.getY(), point.getX()));
                results.put(s, v);
            }
            return results;
        }, geom);
        return resultMap;
    }



    @Override
    public Map<String, Object> isWithinOriginOrDestination(List<LatLng> line, SubrouteKey subroute) {
        String sql = "SELECT " +
                " (CASE WHEN orig.geom IS NULL THEN false ELSE ST_Within(?, orig.geom) END) withinOrig, " +
                " (CASE WHEN dest.geom IS NULL THEN false ELSE ST_Within(?, dest.geom) END) withinDest," +
                " subroute.origin_id originId, subroute.dest_id destId, subroute.next_id nextId " +
                " FROM subroute_new as subroute " +
                " JOIN route_new as route ON subroute.name = route.name" +
                " LEFT JOIN ref.geofence orig ON subroute.origin_id = orig.gid" +
                " LEFT JOIN ref.geofence dest ON subroute.dest_id = dest.gid " +
                " WHERE route.gpsenabled = true AND subroute.route = ? and subroute.direction = ?";
        PGgeometry pgGeometry = Mappers.toPGGeometry(line, Mappers.NAD83_SRID);
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, pgGeometry, pgGeometry, subroute.getRoute(), subroute.getDirection());
        return results.isEmpty() ? null : results.get(0);
    }
}
