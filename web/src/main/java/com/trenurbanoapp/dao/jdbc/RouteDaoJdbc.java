package com.trenurbanoapp.dao.jdbc;

import com.trenurbanoapp.dao.RouteDao;
import com.trenurbanoapp.model.Route;
import com.trenurbanoapp.scraper.model.LatLng;
import org.postgis.PGgeometry;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.*;

import javax.sql.DataSource;
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
                "  route.id, " +
                "  route.id as name, " +
                "  route.desc fullName, " +
                "  route.priority, " +
                "  route.color " +
                "FROM ref.subroute_new as subroute " +
                "JOIN route_new as route ON subroute.route = route.id " +
                "LEFT JOIN schedule ON subroute.route = schedule.route " +
                "where st_dwithin(subroute.geom, st_transform(st_setSrid(st_point(?, ?), 4326), 32161), 300) " +
                "and schedule.route is null " +
                "order by route.priority ";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Route.class), lng, lat);
    }

    @Override
    @Cacheable("allRoutes")
    public List<Route> getAllRoutes() {
        return jdbcTemplate.query("SELECT route.id, route.color, route.desc, " +
                        " ST_AsGeoJSON(ST_Transform(ST_Multi(ST_Union(ST_Simplify(subroute.geom, ?))), 4326)) geojson " +
                        " FROM route_new as route " +
                        " JOIN subroute_new as subroute ON route.id = subroute.route " +
                        " GROUP BY route.id, route.color, route.sort_order " +
                        " ORDER BY route.sort_order ",
                rowMapper,
                SIMPLIFY_TOLERANCE);
    }

    @Override
    public List<String> getRoutesNamesWithinDistance(LatLng point, int distanceInMeters) {
        return jdbcTemplate.query("SELECT DISTINCT route.id, route.priority " +
                " FROM route_new as route " +
                " JOIN subroute_new AS subroute ON route.id = subroute.route " +
                " WHERE st_dwithin(subroute.geom, st_transform(?, 32161), ?) ORDER BY route.priority ", (resultSet, i) -> {
                    return resultSet.getString("id");
                }, new PGgeometry(Mappers.toPoint(point)), distanceInMeters);
    }

    @Override
    public Set<String> getOriginDestinationRouteNamesWithinDistance(LatLng origin, LatLng destination, int distanceInMeters) {
        final Set<String> results = new HashSet<>();
        jdbcTemplate.query("SELECT DISTINCT orig.route route_orig, dest.route route_dest " +
                        " FROM subroute_new AS orig " +
                        " JOIN subroute_new AS dest ON st_intersects(orig.geom, dest.geom) " +
                        " WHERE st_dwithin(orig.geom, st_transform(?, 32161), ?) " +
                        " AND st_dwithin(dest.geom, st_transform(?, 32161), ?) ",
                rs -> {
                    while (rs.next()) {
                        results.add(rs.getString("route_orig"));
                        results.add(rs.getString("route_dest"));
                    }
                    return null;
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
        String sql = "SELECT subroute_stop.route " +
                " FROM stop " +
                " JOIN subroute_stop ON stop.gid = subroute_stop.stop " +
                " WHERE stop.gid = ? ";
        return jdbcTemplate.query(sql, new SingleColumnRowMapper<>(), stopGid);
    }

    private static final RowMapper<Route> rowMapper = (rs, i) -> {
        Route route = new Route();
        route.setId(rs.getString("id"));
        route.setColor(rs.getString("color"));
        route.setGeometryAsGeoJson(rs.getString("geojson"));
        route.setName(rs.getString("id"));
        route.setFullName(rs.getString("desc"));
        return route;
    };
}
