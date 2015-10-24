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
            "  stop_area.name station, " +
            "  stop_area.full_name stop_area, " +
            "  dest.full_name      dest, " +
            "  trainschedule.schedule_type       schedule_type, " +
            "  trainschedule.arrival             stop_time_as_sql_time " +
            "FROM route " +
            "  JOIN trainschedule ON route.name = trainschedule.route " +
            "  JOIN stop_area ON stop_area.name = trainschedule.station " +
            "  JOIN stop_area dest ON dest.name = trainschedule.direction " +
            "WHERE " +
            " trainschedule.direction <> trainschedule.station " +
            "  AND trainschedule.arrival > ? ";

    @Override
    public List<StopTime> getNextStopTimes(String stopArea, LocalTime localTime) {
        String sql = STOP_TIME_QUERY + " AND stop_area.name = ? ORDER BY arrival LIMIT 10 ";
        return jdbcTemplate.query(sql, stopTimeMapper, Time.valueOf(localTime), stopArea);
    }

    @Override
    public List<StopTime> getNextStopTimes(double lat, double lng, LocalTime localTime) {
        String sql = STOP_TIME_QUERY +
                " AND stop_area.name = (select name from stop_area order by st_distance(geom, st_setsrid(st_point(?, ?), 4326) ) limit 1) " +
                " ORDER BY arrival limit 10 ";
        return jdbcTemplate.query(sql, stopTimeMapper, Time.valueOf(localTime), lng, lat);
    }

    @Override
    @Cacheable("allStopAreas")
    public List<IdDesc> findAllStopAreas() {
        String sql = "SELECT * FROM stop_area";
        return jdbcTemplate.query(sql, ScheduleDaoJdbc::mapStopArea);
    }

    @Override
    public IdDesc findNearestStopArea(double lat, double lng) {
        String sql = "select * from stop_area order by st_distance(geom, st_setsrid(st_point(?, ?), 4326) ) limit 1";
        return jdbcTemplate.queryForObject(sql, ScheduleDaoJdbc::mapStopArea, lng, lat);
    }

    private static IdDesc mapStopArea(ResultSet rs, int rowNum) throws SQLException{
        return new IdDesc(rs.getString("name"), rs.getString("full_name"));
    }

}
