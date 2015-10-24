package com.trenurbanoapp.dao.jdbc;

import com.trenurbanoapp.dao.SubrouteDao;
import com.trenurbanoapp.model.Subroute;
import com.trenurbanoapp.model.SubrouteView;
import com.trenurbanoapp.scraper.model.LatLng;
import org.postgis.PGgeometry;
import org.postgis.Point;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by victor on 4/23/14.
 */
public class SubrouteDaoJdbc implements SubrouteDao {

    private JdbcOperations jdbcTemplate;
    private RowMapper<Subroute> rowMapper = new RowMapper<Subroute>() {
        @Override
        public Subroute mapRow(ResultSet rs, int i) throws SQLException {
            Subroute s = new Subroute();
            s.setGid(rs.getInt("gid"));
            s.setRouteName(rs.getString("name"));
            s.setDestination(rs.getString("dest"));
            s.setDestinationId((Integer) rs.getObject("dest_id"));
            s.setNextSubrouteId((Integer) rs.getObject("next_id"));
            s.setOriginId((Integer) rs.getObject("origin_id"));
            return s;
        }
    };

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Map<String, Object>> getDistanceAlongNearbySubroutes2(LatLng position) {
        String sql = "SELECT * FROM ( " +
                "SELECT subroute_intersection_view.gid, subroute_intersection_view.name subroute, dest, vehicle.name vehicle_name, vehicle.asset_id, " +
                " cast(ST_InterpolatePoint(geom_with_m, ST_Line_Interpolate_Point((geom_dump).geom, 0.5)) - vehicle_state.subroute_m AS INT) distance, " +
                " vehicle_state.avg_speed, vehicle_state.location_desc, vehicle_state.cardinal_dir, " +
                " vehicle_state.stop_gid, route_color, lpad(cast(stop.ama_id as varchar), 4, '0') stop_ama_id, stop.descriptio " +
                "FROM ( " +
                "   SELECT subroute.gid, subroute.name, subroute.dest, subroute.geom_with_m, route.color route_color, " +
                "   ST_Dump ( " +
                "      ST_Intersection ( " +
                "         subroute.geom, ST_Buffer(position.point, 300) " +
                "      ) " +
                "   ) geom_dump " +
                "   FROM ref.subroute as subroute JOIN ref.route as route ON subroute.name = route.name, " +
                "   (SELECT ? point) position " +
                "   WHERE route.gpsenabled = TRUE AND ST_DWithin(subroute.geom, position.point, 300) " +
                ") subroute_intersection_view " +
                "JOIN ref.vehicle_state as vehicle_state ON vehicle_state.last_known_subroute_id = subroute_intersection_view.gid " +
                "JOIN ref.vehicle as vehicle ON vehicle_state.asset_id = vehicle.asset_id " +
                "LEFT JOIN ref.stop as stop ON vehicle_state.stop_gid = stop.gid " +
                "WHERE vehicle_state.subroute_m IS NOT NULL " +
                ") vehicle_distances " +
                "WHERE vehicle_distances.distance > 0 ";

        PGgeometry geom = new PGgeometry(Mappers.toPoint(position, Mappers.NAD83_SRID));
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, geom);
        return results;
    }

    @Override
    public Map<Integer, SubrouteView> getGpsEnabledSubroutesWithinDistance(List<LatLng> line, int distanceInMeters, Integer... subset) {

        if (line.size() == 1) {
            throw new IllegalArgumentException("linestring must have more than 1 point");
        }

        PGgeometry geom = new PGgeometry(Mappers.toLineString(line, Mappers.NAD83_SRID));
        String sql = "SELECT gid, name, dest, dest_id, next_id, origin_id, subroute_position, " +
                " trail_azimuth, subroute_azimuth, trail_length, subroute_length, subroute_m  " +
                " FROM ( " +
                "     SELECT " +
                "        gid, name, dest, dest_id, next_id, origin_id, " +
                "        ST_PointN((geom_dump).geom, 1) subroute_position, " +
                "        ST_Length(trail) trail_length, " +
                "        ST_Length((geom_dump).geom) subroute_length, " +
                "        ST_Azimuth(ST_Startpoint(trail), st_endpoint(trail)) trail_azimuth, " +
                "        ST_Azimuth(ST_Startpoint((geom_dump).geom),st_endpoint((geom_dump).geom)) subroute_azimuth," +
                "        ST_InterpolatePoint(geom_with_m, ST_PointN((geom_dump).geom, 1)) subroute_m " +
                "     FROM ( " +
                "        SELECT subroute.gid, subroute.name, subroute.dest, subroute.dest_id, subroute.next_id, subroute.origin_id, " +
                "            vehicle_trail.trail, subroute.geom_with_m, " +
                "            ST_Dump ( " +
                "               ST_Intersection ( " +
                "                  subroute.geom, ST_Buffer(vehicle_trail.trail,30,'endcap=flat') " +
                "               ) " +
                "            ) geom_dump " +
                "        FROM " +
                "            ref.subroute as subroute JOIN ref.route as route ON subroute.name = route.name, " +
                "            (SELECT ST_Reverse(?) AS trail ) vehicle_trail " +
                "        WHERE route.gpsenabled = TRUE AND ST_DWithin(subroute.geom, vehicle_trail.trail, 30) " +
                "     ) subroute_intersection_view " +
                " ) segment_azimuths " +
                " WHERE least ( " +
                "   2 * pi() - abs(trail_azimuth - subroute_azimuth), " +
                "   abs(trail_azimuth - subroute_azimuth) " +
                " ) < (0.4 * pi()) " +
                " AND abs(trail_length - subroute_length) < trail_length * 0.3 ";
        return jdbcTemplate.query(sql, (ResultSet rs) -> {
            Map<Integer, SubrouteView> results = new HashMap<>();
            for (int i = 0; rs.next(); i++) {
                SubrouteView v = new SubrouteView();
                v.setSubroute(rowMapper.mapRow(rs, i));
                v.setSubrouteSegmentAzimuth(rs.getDouble("subroute_azimuth"));
                v.setVehicleTrailAzimuth(rs.getDouble("trail_azimuth"));
                v.setSubrouteSegmentLength(rs.getDouble("subroute_length"));
                v.setVehicleTrailLength(rs.getDouble("trail_length"));
                v.setSubrouteM(rs.getDouble("subroute_m"));
                PGgeometry geom1 = (PGgeometry) rs.getObject("subroute_position");
                Point point = (Point) geom1.getGeometry();
                v.setSubroutePosition(new LatLng(point.getY(), point.getX()));
                results.put(v.getSubroute().getGid(), v);
            }
            return results;
        }, geom);
    }

    @Override
    public float getMeasure(int subrouteId, LatLng point) {
        String sql = "SELECT ST_InterpolatePoint(ref.subroute.geom_with_m, ?) FROM ref.subroute WHERE gid = ? ";
        return jdbcTemplate.queryForObject(sql, Float.class, Mappers.toPGGeometry(point, Mappers.NAD83_SRID), subrouteId);
    }

    @Override
    @Cacheable("subroutesByIdCache")
    public Subroute getSubroute(int id) {
        List<Subroute> results = jdbcTemplate.query("SELECT gid, name, dest, dest_id, next_id, origin_id FROM ref.subroute WHERE gid = ?", rowMapper, id);
        return !results.isEmpty() ? results.get(0) : null;
    }


    @Override
    public Map<String, Object> isWithinOriginOrDestination(List<LatLng> line, int subrouteGid) {
        String sql = "SELECT " +
                " (CASE WHEN orig.geom IS NULL THEN false ELSE ST_Within(?, orig.geom) END) withinOrig, " +
                " (CASE WHEN dest.geom IS NULL THEN false ELSE ST_Within(?, dest.geom) END) withinDest," +
                " subroute.origin_id originId, subroute.dest_id destId, subroute.next_id nextId " +
                " FROM ref.subroute as subroute " +
                " JOIN ref.route as route ON subroute.name = route.name" +
                " LEFT JOIN ref.geofence orig ON subroute.origin_id = orig.gid" +
                " LEFT JOIN ref.geofence dest ON subroute.dest_id = dest.gid " +
                " WHERE route.gpsenabled = true AND subroute.gid = ? ";
        PGgeometry pgGeometry = Mappers.toPGGeometry(line, Mappers.NAD83_SRID);

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, pgGeometry, pgGeometry, subrouteGid);

        return results.isEmpty() ? null : results.get(0);
    }
}
