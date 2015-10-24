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

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 * Created by victor on 3/2/14.
 */
public class StopDaoJdbc implements StopDao {

    private JdbcOperations jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    @Cacheable(value = "stopsByRouteCache")
    public List<Stop> getStopsByRouteName(String routeName) {
        String sql = "select stop.* from ref.stop as stop" +
                " join ref.stop_route as stop_route on stop.gid = stop_route.stop_gid " +
                " join ref.route as route on stop_route.route_gid = route.gid " +
                " where route.name = ? ";
        return jdbcTemplate.query(sql, MAPPER, routeName);
    }

    @Override
    public List<Stop> getStopsByRouteNames(GetStopsRequest request) {
        String selectAndFromClause = "select stop.* from ref.stop as stop " +
                " join ref.stop_route as stop_route on stop.gid = stop_route.stop_gid " +
                " join ref.route as route on stop_route.route_gid = route.gid ";
        CriteriaQueryBuilder builder = new CriteriaQueryBuilder(selectAndFromClause);
        Set<String> routeNames = request.getRouteNames();
        builder.whereIn("route.name", (Object[]) routeNames.toArray(new String[routeNames.size()]));
        builder.whereClause("stop.geom && st_makeenvelope(?, ?, ?, ?)",
                request.getBounds().getSouthwest().getLng(),
                request.getBounds().getSouthwest().getLat(),
                request.getBounds().getNortheast().getLng(),
                request.getBounds().getNortheast().getLat());
        return jdbcTemplate.query(builder.sql(), MAPPER, builder.values());
    }

    @Override
    public Stop getClosestStopWithin10Meters(List<LatLng> trail) {
        String sql = "select * from ref.stop where ST_DWithin(?, stop.geom, 10) order by ST_distance(?, stop.geom) asc limit 1 ";
        List<Stop> result = jdbcTemplate.query(sql, MAPPER, Mappers.toPGGeometry(trail), Mappers.toPGGeometry(trail));
        return result.isEmpty() ? null : result.get(0);
    }



    @Override
    @Cacheable("stopById")
    public Stop getStopById(int stopId) {
        List<Stop> result = jdbcTemplate.query("select * from ref.stop where gid = ?", MAPPER, stopId);
        return result.isEmpty() ? null : result.get(0);
    }

    private static final RowMapper<Stop> MAPPER = new RowMapper<Stop>() {

        @Override
        public Stop mapRow(ResultSet rs, int i) throws SQLException {
            Stop stop = new Stop();
            stop.setId(rs.getInt("gid"));
            PGgeometry geom = (PGgeometry) rs.getObject("geom");
            Point point = (Point) geom.getGeometry();
            stop.setLocation(new LatLng(point.getY(), point.getX()));
            String amaId = rs.getString("ama_id");
            stop.setAmaId(amaId != null ? amaId : "");
            return stop;
        }
    };
}
