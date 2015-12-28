package com.trenurbanoapp.dao.jdbc;

import com.trenurbanoapp.dao.SubrouteDao;
import com.trenurbanoapp.model.SubrouteKey;
import com.trenurbanoapp.model.SubrouteView;
import com.trenurbanoapp.scraper.model.LatLng;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.postgis.PGgeometry;
import org.postgis.Point;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by victor on 4/23/14.
 */
@Repository
public class SubrouteDaoJdbc implements SubrouteDao {

    private static final Logger log = LogManager.getLogger(SubrouteDaoJdbc.class);

    private JdbcOperations jdbcTemplate;
    private RowMapper<SubrouteKey> rowMapper = (rs, i) -> {
        SubrouteKey s = new SubrouteKey();
        s.setRoute(rs.getString("route"));
        s.setDirection(rs.getString("direction"));
        return s;
    };

    @Inject
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }



    @Override
    public List<String> getGpsEnabledRoutesWithinDistance(List<LatLng> line, int distanceInMeters) {
        PGgeometry geom = new PGgeometry(Mappers.toLineString(line, Mappers.WGS84_SRID));
        return jdbcTemplate.query("SELECT DISTINCT route FROM subroute AS subroute WHERE ST_DWithin(ST_Transform(?, 32161), ?)",
                new SingleColumnRowMapper<>(String.class), geom, distanceInMeters);
    }

    @Override
    public Map<SubrouteKey, SubrouteView> getGpsEnabledSubroutesWithin100Meters(LatLng currPos, LatLng prevPos) {
        return jdbcTemplate.query("WITH tmp AS ( " +
                        " SELECT " +
                        "   ST_Transform(ST_SetSRID(ST_Point(?,?), 4326), 32161) curr_pos, " +
                        "   ST_Transform(ST_SetSRID(ST_Point(?,?), 4326), 32161) prev_pos " +
                        ") SELECT route, direction, " +
                        "   ST_InterpolatePoint(subroute.geom, tmp.curr_pos) subroute_m, " +
                        "   ST_Azimuth(tmp.prev_pos, tmp.curr_pos) azimuth " +
                        " FROM tmp " +
                        " JOIN subroute ON ST_DWithin(subroute.geom, tmp.curr_pos, 100) " +
                        " JOIN route ON subroute.route = route.id " +
                        " WHERE route.gpsenabled " +
                        " AND ST_InterpolatePoint(subroute.geom, tmp.curr_pos) - ST_InterpolatePoint(subroute.geom, tmp.prev_pos) > 0 ",
                (ResultSet rs) -> {
                    Map<SubrouteKey, SubrouteView> results = new HashMap<>();
                    for (int i = 0; rs.next(); i++) {
                        SubrouteView v = new SubrouteView();
                        SubrouteKey s = rowMapper.mapRow(rs, i);
                        v.setSubrouteKey(s);
                        v.setSubrouteM(rs.getDouble("subroute_m"));
                        v.setAzimuth(rs.getDouble("azimuth"));
                        results.put(s, v);
                    }
                    return results;
                },
                currPos.getLng(), currPos.getLat(),
                prevPos.getLng(), prevPos.getLat());
    }

    @Override
    public List<Map<String, Object>> getEtas(LatLng position) {
        return jdbcTemplate.queryForList("WITH tmp AS ( " +
                        " SELECT ST_Transform(ST_SetSRID(ST_Point(?,?), 4326), 32161) curr_pos " +
                        ") SELECT subroute.route, subroute.direction, route.color, route.desc fullName, " +
                        "   cast(ST_InterpolatePoint(subroute.geom, tmp.curr_pos) - vehicle_state.subroute_m as int) distance, " +
                        "   cast(vehicle_state.avg_speed as numeric(4,1)) avg_speed, " +
                        "   cast((ST_InterpolatePoint(subroute.geom, tmp.curr_pos) - vehicle_state.subroute_m) / vehicle_state.avg_speed as int) as eta " +
                        "FROM tmp " +
                        " JOIN subroute ON ST_DWithin(subroute.geom, tmp.curr_pos, 100) " +
                        " JOIN route ON route.gpsenabled and subroute.route = route.id\n" +
                        " JOIN vehicle_state on subroute.route = vehicle_state.last_known_route " +
                        "     and subroute.direction = vehicle_state.last_known_direction\n" +
                        " where ST_InterpolatePoint(subroute.geom, tmp.curr_pos) > vehicle_state.subroute_m " +
                        " order by (ST_InterpolatePoint(subroute.geom, tmp.curr_pos) - vehicle_state.subroute_m) / vehicle_state.avg_speed ",
                position.getLng(), position.getLat());
    }


    @Override
    public Map<String, Object> isWithinOriginOrDestination(List<LatLng> line, SubrouteKey subroute) {
        String sql = "WITH trail AS ( " +
                "  SELECT ST_Transform(?, 32161) AS geom " +
                ") " +
                "SELECT " +
                "  ST_DWithin(trail.geom, ST_PointN(subroute.geom, 1), 100) withinOrig, " +
                "  ST_DWithin(trail.geom, ST_EndPoint(subroute.geom), 100) withinDest, " +
                "  subroute.direction curr_direction, " +
                "  next_subroute.direction AS next_direction " +
                " FROM trail, subroute AS subroute " +
                "  JOIN subroute AS next_subroute ON subroute.route = next_subroute.route " +
                "    AND subroute.direction <> next_subroute.direction " +
                "  JOIN route AS route ON subroute.route = route.id " +
                " WHERE route.gpsenabled = TRUE  " +
                "  AND subroute.route = ? " +
                "  AND subroute.direction = ? ";
        PGgeometry pgGeometry = Mappers.toPGGeometry(line, Mappers.WGS84_SRID);
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, pgGeometry, subroute.getRoute(), subroute.getDirection());
        return results.isEmpty() ? null : results.get(0);
    }


    @Override
    public List<Map<String, Object>> getNearbySubroutesWithoutScheduleOrEta(double lat, double lng) {
        String sql = "SELECT DISTINCT " +
                "  subroute.route, " +
                "  coalesce(stop_area.desc, lower(subroute.direction)) direction, " +
                "  route.desc fullName, " +
                "  route.priority, " +
                "  route.color " +
                "FROM subroute " +
                " LEFT JOIN stop_area ON subroute.direction = stop_area.id " +
                " JOIN route ON subroute.route = route.id " +
                " LEFT JOIN schedule ON subroute.route = schedule.route " +
                " LEFT JOIN vehicle_state ON subroute.route = vehicle_state.last_known_route and subroute.direction = vehicle_state.last_known_direction " +
                "WHERE st_dwithin(subroute.geom, st_transform(st_setSrid(st_point(?, ?), 4326), 32161), 300) " +
                " AND schedule.route IS NULL AND vehicle_state.asset_id IS NULL " +
                " ORDER BY route.priority, subroute.route ";
        return jdbcTemplate.queryForList(sql, lng, lat);
    }
}
