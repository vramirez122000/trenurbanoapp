package com.trenurbanoapp.dao.jdbc;

import com.trenurbanoapp.dao.StopDao;
import com.trenurbanoapp.model.GetStopsRequest;
import com.trenurbanoapp.model.Stop;
import com.trenurbanoapp.scraper.model.LatLng;
import org.postgis.PGgeometry;
import org.postgis.Point;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 * Created by victor on 3/2/14.
 */
@Repository
public class StopDaoJdbc implements StopDao {

    private JdbcOperations jdbcTemplate;

    @Inject
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    @Cacheable(value = "stopsByRouteCache")
    public List<Stop> getStopsByRouteName(String routeName) {
        String sql = "select stop.* from stop " +
                " join subroute_stop on stop.gid = subroute_stop.stop " +
                " where subroute_stop.route = ? ";
        return jdbcTemplate.query(sql, MAPPER, routeName);
    }

    @Override
    public List<Stop> getStopsByRouteNames(GetStopsRequest request) {
        String selectAndFromClause = "select stop.* from stop " +
                " join subroute_stop on stop.gid = subroute_stop.stop ";
        CriteriaQueryBuilder builder = new CriteriaQueryBuilder(selectAndFromClause);
        Set<String> routeNames = request.getRouteNames();
        builder.whereIn("subroute_stop.route", (Object[]) routeNames.toArray(new Object[routeNames.size()]));
        builder.whereClause("stop.geom && st_makeenvelope(?, ?, ?, ?)",
                request.getBounds().getSouthwest().getLng(),
                request.getBounds().getSouthwest().getLat(),
                request.getBounds().getNortheast().getLng(),
                request.getBounds().getNortheast().getLat());
        return jdbcTemplate.query(builder.sql(), MAPPER, builder.values());
    }

    @Override
    public Stop getClosestStopWithin10Meters(List<LatLng> trail) {
        String sql = "select * from stop where ST_DWithin(ST_transform(?, 32161), ST_Transform(stop.geom, 32161), 10) order by ST_distance(?, stop.geom) asc limit 1 ";
        List<Stop> result = jdbcTemplate.query(sql, MAPPER, Mappers.toPGGeometry(trail), Mappers.toPGGeometry(trail));
        return result.isEmpty() ? null : result.get(0);
    }



    @Override
    @Cacheable("stopById")
    public Stop getStopById(int stopId) {
        List<Stop> result = jdbcTemplate.query("select * from stop where gid = ?", MAPPER, stopId);
        return result.isEmpty() ? null : result.get(0);
    }

    private static final RowMapper<Stop> MAPPER = (rs, i) -> {
        Stop stop = new Stop();
        stop.setId(rs.getInt("gid"));
        PGgeometry geom = (PGgeometry) rs.getObject("geom");
        Point point = (Point) geom.getGeometry();
        stop.setLocation(new LatLng(point.getY(), point.getX()));
        String amaId = rs.getString("ama_id");
        stop.setAmaId(amaId != null ? amaId : "");
        return stop;
    };
}
