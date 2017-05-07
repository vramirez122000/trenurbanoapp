package com.trenurbanoapp.dao.jdbc;

import com.trenurbanoapp.dao.ScheduleDao;
import com.trenurbanoapp.model.IdDesc;
import com.trenurbanoapp.model.RouteGroup;
import com.trenurbanoapp.model.ScheduleType;
import com.trenurbanoapp.model.StopTime;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

/**
 * User: victor
 * Date: 9/24/11
 */
@Repository
public class ScheduleDaoJdbc implements ScheduleDao {

    private static final RowMapper<StopTime> stopTimeMapper = (rs, rowNum) -> {
        StopTime s = new StopTime();
        s.setRoute(rs.getString("route"));
        s.setRouteCode(rs.getString("route_code"));
        s.setRouteFullName(rs.getString("route_full_name"));
        String routeGroupStr = rs.getString("route_group");
        s.setRouteGroup(RouteGroup.valueOf(routeGroupStr));
        s.setColor(rs.getString("color"));
        s.setStation(rs.getString("station"));
        s.setStopArea(rs.getString("stop_area"));
        s.setDest(rs.getString("dest"));
        s.setScheduleType(ScheduleType.valueOf(rs.getString("schedule_type")));
        s.setStopTime(rs.getTime("stop_time").toLocalTime());
        s.setErrorMinutes(rs.getInt("error_minutes"));
        return s;
    };

    private JdbcTemplate jdbcTemplate;

    @Inject
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private static final String STOP_TIME_QUERY = "SELECT " +
            "  route.id          route, " +
            "  route.code     route_code, " +
            "  route.desc     route_full_name, " +
            "  route.route_group, " +
            "  route.color, " +
            "  stop_area.id station, " +
            "  stop_area.desc stop_area, " +
            "  dest.desc      dest, " +
            "  schedule.schedule_type, " +
            "  schedule.stop_time, " +
            "  schedule.error_minutes              " +
            "FROM route as route " +
            "  JOIN schedule ON route.id = schedule.route " +
            "  JOIN stop_area ON stop_area.id = schedule.stop_area " +
            "  JOIN stop_area dest ON dest.id = schedule.direction " +
            "WHERE " +
            " schedule.direction <> schedule.stop_area " +
            "  AND schedule.stop_time > ? ";

    @Override
    public List<StopTime> getNextStopTimes(String stopArea, LocalTime localTime) {
        String sql = STOP_TIME_QUERY + " AND stop_area.id = ? ORDER BY stop_time LIMIT 10 ";
        return jdbcTemplate.query(sql, stopTimeMapper, Time.valueOf(localTime), stopArea);
    }

    @Override
    public List<StopTime> getNextStopTimes(double lat, double lng, LocalTime localTime) {
        String sql = STOP_TIME_QUERY +
                " AND stop_area.id = (select id from stop_area order by st_distance(geom, st_setsrid(st_point(?, ?), 4326) ) limit 1) " +
                " ORDER BY stop_time limit 10 ";
        return jdbcTemplate.query(sql, stopTimeMapper, Time.valueOf(localTime), lng, lat);
    }

    @Override
    @Cacheable("allStopAreas")
    public List<IdDesc> findAllStopAreas() {
        String sql = "SELECT * FROM stop_area where sched_origin = true order by sort_order";
        return jdbcTemplate.query(sql, ScheduleDaoJdbc::mapStopArea);
    }

    @Override
    public IdDesc findNearestStopArea(double lat, double lng) {
        String sql = "select * from stop_area where sched_origin = true " +
                " order by st_distance(geom, st_setsrid(st_point(?, ?), 4326) ) limit 1";
        return jdbcTemplate.queryForObject(sql, ScheduleDaoJdbc::mapStopArea, lng, lat);
    }

    @Override
    public List<IdDesc> findStopAreasByDistance(double lat, double lng) {
        String sql = "select * from stop_area where sched_origin = true " +
                " order by st_distance(geom, st_setsrid(st_point(?, ?), 4326) )";
        return jdbcTemplate.query(sql, ScheduleDaoJdbc::mapStopArea, lng, lat);
    }

    private static IdDesc mapStopArea(ResultSet rs, int rowNum) throws SQLException{
        return new IdDesc(rs.getString("id"), rs.getString("desc"));
    }

}
