package com.trenurbanoapp.dao.jdbc;

import com.trenurbanoapp.dao.RouteDao;
import com.trenurbanoapp.model.Route;
import com.trenurbanoapp.scraper.model.LatLng;
import org.postgis.PGgeometry;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by victor on 3/2/14.
 */
public class RouteDaoJdbc implements RouteDao {

    private static final int SIMPLIFY_TOLERANCE = 10;

    private JdbcOperations jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Route> getNearbyRoutesWithoutSchedule(double lat, double lng) {
        String sql = "select distinct " +
                "    route.name, " +
                "    route.full_name, " +
                "    route.priority, " +
                "    route.color " +
                "FROM route " +
                "LEFT JOIN trainschedule ON route.name = trainschedule.route " +
                "where st_dwithin(st_transform(route.geom, 32161), st_transform(st_setSrid(st_point(?, ?), 4326), 32161), 300) " +
                "and trainschedule.route is null " +
                "order by route.priority ";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Route.class), lng, lat);
    }

    @Override
    @Cacheable("allRoutes")
    public List<Route> getAllRoutes() {
        return jdbcTemplate.query("SELECT route.gid, route.name, route.color, " +
                        " ST_AsGeoJSON(ST_Transform(ST_Multi(ST_Union(ST_Simplify(subroute.geom, ?))), 4326)) geojson " +
                        " FROM route " +
                        " JOIN subroute ON route.name = subroute.name " +
                        " GROUP BY route.gid, route.name, route.color, route.sort_order " +
                        " ORDER BY route.sort_order ",
                rowMapper,
                SIMPLIFY_TOLERANCE);
    }

    @Override
    public List<String> getRoutesNamesWithinDistance(LatLng point, int distanceInMeters) {
        return jdbcTemplate.query("SELECT DISTINCT route.name, route.priority " +
                        " FROM ref.route AS route " +
                        " JOIN ref.subroute AS subroute ON route.name = subroute.name " +
                        " WHERE st_dwithin(subroute.geom, st_transform(?, 32161), ?) ORDER BY route.priority ",
                new RowMapper<String>() {
                    @Override
                    public String mapRow(ResultSet resultSet, int i) throws SQLException {
                        return resultSet.getString("name");
                    }
                },
                new PGgeometry(Mappers.toPoint(point)),
                distanceInMeters);
    }

    @Override
    public Set<String> getOriginDestinationRouteNamesWithinDistance(LatLng origin, LatLng destination, int distanceInMeters) {
        final Set<String> results = new HashSet<>();
        jdbcTemplate.query("SELECT DISTINCT orig.name route_orig, dest.name route_dest " +
                        " FROM ref.subroute AS orig " +
                        " JOIN ref.subroute AS dest ON st_intersects(orig.geom, dest.geom) " +
                        " WHERE st_dwithin(orig.geom, st_transform(?, 32161), ?) " +
                        " AND st_dwithin(dest.geom, st_transform(?, 32161), ?) ",
                new ResultSetExtractor<Void>() {
                    @Override
                    public Void extractData(ResultSet rs) throws SQLException, DataAccessException {
                        while (rs.next()) {
                            results.add(rs.getString("route_orig"));
                            results.add(rs.getString("route_dest"));
                        }
                        return null;
                    }
                },
                new PGgeometry(Mappers.toPoint(origin)),
                distanceInMeters,
                new PGgeometry(Mappers.toPoint(destination)),
                distanceInMeters);
        return results;
    }

    @Override
    @Cacheable("routesByStopCache")
    public List<String> getRouteNamesByStop(int stopGid) {
        String sql = "SELECT route.name " +
                " FROM ref.stop AS stop " +
                " JOIN ref.stop_route AS stop_route ON stop.gid = stop_route.stop_gid " +
                " JOIN ref.route AS route ON route.gid = stop_route.route_gid " +
                " WHERE stop.gid = ? ";
        return jdbcTemplate.query(sql, new SingleColumnRowMapper<>(), stopGid);
    }

    private static final RowMapper<Route> rowMapper = (rs, i) -> {
        Route route = new Route();
        route.setId(rs.getInt("gid"));
        route.setColor(rs.getString("color"));
        route.setGeometryAsGeoJson(rs.getString("geojson"));
        route.setName(rs.getString("name"));
        return route;
    };
}
