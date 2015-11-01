package com.trenurbanoapp.dao.jdbc;

import com.trenurbanoapp.dao.ScheduleDao;
import com.trenurbanoapp.model.IdDesc;
import com.trenurbanoapp.model.StopTime;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
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

    private static final BeanPropertyRowMapper<StopTime> stopTimeMapper = new BeanPropertyRowMapper<>(StopTime.class);

    private JdbcTemplate jdbcTemplate;

    @Inject
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private static final String STOP_TIME_QUERY = "SELECT " +
            "  route.name          route, " +
            "  route.full_name     route_full_name, " +
            "  route.route_group   route_group, " +
            "  route.color, " +
            "  stop_area.id station, " +
            "  stop_area.desc stop_area, " +
            "  dest.desc      dest, " +
            "  schedule.schedule_type       schedule_type, " +
            "  schedule.stop_time             stop_time_as_sql_time " +
            "FROM route " +
            "  JOIN schedule ON route.name = schedule.route " +
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
        String sql = "SELECT * FROM stop_area order by sort_order";
        return jdbcTemplate.query(sql, ScheduleDaoJdbc::mapStopArea);
    }

    @Override
    public IdDesc findNearestStopArea(double lat, double lng) {
        String sql = "select * from stop_area order by st_distance(geom, st_setsrid(st_point(?, ?), 4326) ) limit 1";
        return jdbcTemplate.queryForObject(sql, ScheduleDaoJdbc::mapStopArea, lng, lat);
    }

    @Override
    public List<IdDesc> findStopAreasByDistance(double lat, double lng) {
        String sql = "select * from stop_area order by st_distance(geom, st_setsrid(st_point(?, ?), 4326) )";
        return jdbcTemplate.query(sql, ScheduleDaoJdbc::mapStopArea, lng, lat);
    }

    private static IdDesc mapStopArea(ResultSet rs, int rowNum) throws SQLException{
        return new IdDesc(rs.getString("id"), rs.getString("desc"));
    }

}
