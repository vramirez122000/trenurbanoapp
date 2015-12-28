package com.trenurbanoapp.dao.jdbc;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.trenurbanoapp.dao.VehicleStateDao;
import com.trenurbanoapp.model.SubrouteKey;
import com.trenurbanoapp.model.VehicleState;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * User: victor
 * Date: 5/11/13
 * Time: 8:59 AM
 */
@Repository
public class VehicleStateDaoJdbc implements VehicleStateDao {

    public static final Splitter COMMA_SPLITTER = Splitter.on(",").omitEmptyStrings();
    public static final Joiner COMMA_JOINER = Joiner.on(",").skipNulls();
    public static final PolyLineMapper TRAIL_MAPPER = new PolyLineMapper("trail");
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private SimpleJdbcInsert vehicleStateInsert;
    private SimpleJdbcInsert possibleSubroutesInsert;
    private SimpleJdbcInsert possibleGeofenceRoutesInsert;
    private Set<String> possibleRouteExistsCache = Collections.synchronizedSet(new HashSet<>(1346));

    @Value("${gps.routeAlgorithm.useRoutes}")
    private Boolean useRoutes;

    @Inject
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.vehicleStateInsert = new SimpleJdbcInsert(dataSource)
                .withSchemaName("ref")
                .withTableName("vehicle_state");
        this.possibleSubroutesInsert = new SimpleJdbcInsert(dataSource)
                .withSchemaName("ref")
                .withTableName("vehicle_state_possible_subroutes");
        this.possibleGeofenceRoutesInsert = new SimpleJdbcInsert(dataSource)
                .withSchemaName("ref")
                .withTableName("vehicle_state_possible_routes");
    }



    @Override
    public VehicleState getVehicleState(final int assetId) {
        String sql = "SELECT * FROM ref.vehicle_state WHERE asset_id = ?";
        List<VehicleState> results = jdbcTemplate.query(sql, VEHICLE_STATE_MAPPER, assetId);

        if (results.isEmpty()) {
            return null;
        }

        VehicleState v = results.get(0);
        if(useRoutes != null && useRoutes) {
            v.setPossibleSubroutes(getPossibleSubroutes(assetId));
        } else {
            v.setPossibleRoutes(getPossibleRoutes(assetId));
        }

        return v;
    }

    @Override
    public List<VehicleState> getMovingVehicleStatesWithinServiceArea() {
        String sql = "SELECT * FROM ref.vehicle_state " +
                " where current_timestamp - last_trail_change < interval'00:10:00' " +
                " and within_service_area = true ";
        return jdbcTemplate.query(sql, VEHICLE_STATE_MAPPER);
    }

    @Override
    public Map<Integer, VehicleState> getAllAsMap() {
        String sql = "SELECT * FROM ref.vehicle_state";
        return jdbcTemplate.query(sql, rs -> {
            Map<Integer, VehicleState> vstatesMap = new HashMap<>();
            int i = 1;
            while(rs.next()) {
                VehicleState v = VEHICLE_STATE_MAPPER.mapRow(rs, i++);
                vstatesMap.put(v.getAssetId(), v);
            }
            return vstatesMap;
        });
    }

    @Override
    public List<VehicleState> getAll() {
        String sql = "SELECT * FROM ref.vehicle_state";
        return jdbcTemplate.query(sql, VEHICLE_STATE_MAPPER);
    }

    @Override
    public boolean existsVehicleState(int assetId) {
        String sql = "SELECT 1 FROM ref.vehicle_state WHERE asset_id = ?";
        List<Object> results = jdbcTemplate.query(sql, new SingleColumnRowMapper<>(), assetId);
        return (!results.isEmpty());
    }

    @Override
    public void insertVehicleState(VehicleState vehicleState) {
        vehicleStateInsert.execute(VEHICLE_STATE_MAPPER.reverseMap(vehicleState, ReverseRowMapper.Action.INSERT));

        for (SubrouteKey subroute: vehicleState.getPossibleSubroutes().keySet()) {
            insertPossibleSubroute(
                    vehicleState.getAssetId(),
                    subroute,
                    vehicleState.getPossibleSubroutes().get(subroute)
            );
        }

        for (String route : vehicleState.getPossibleRoutes().keySet()) {
            insertPossibleRoute(
                    vehicleState.getAssetId(),
                    route,
                    vehicleState.getPossibleRoutes().get(route)
            );
        }
    }

    @Override
    public void updateLocationDescription(int assetId, String locationDescription) {
        String sql = "update ref.vehicle_state set location_desc = ? where asset_id = ?";
        jdbcTemplate.update(sql, locationDescription, assetId);
    }

    @Override
    public void updateVehicleState(VehicleState vehicleState) {
        UpdateBuilder builder = new UpdateBuilder("vehicle_state");
        builder.appendAll(VEHICLE_STATE_MAPPER.reverseMap(vehicleState, ReverseRowMapper.Action.UPDATE));
        builder.whereEquals("asset_id", vehicleState.getAssetId());
        jdbcTemplate.update(builder.sql(), builder.values());

        List<Map<String, ?>> batchUpdateValues = new ArrayList<>();
        for (SubrouteKey subroute : vehicleState.getPossibleSubroutes().keySet()) {
            Boolean active = vehicleState.getPossibleSubroutes().get(subroute);
            String cacheKey = String.format("%s%s", vehicleState.getAssetId(), subroute);
            if (!possibleRouteExistsCache.contains(cacheKey)) {
                possibleRouteExistsCache.add(cacheKey);
                if(!existsPossibleSubroute(vehicleState.getAssetId(), subroute)) {
                    insertPossibleSubroute(vehicleState.getAssetId(), subroute, active);
                } else {
                    batchUpdateValues.add(createPossibleSubrouteParams(vehicleState.getAssetId(), subroute, active));
                }
            } else {
                batchUpdateValues.add(createPossibleSubrouteParams(vehicleState.getAssetId(), subroute, active));
            }
        }

        namedParameterJdbcTemplate.batchUpdate("update vehicle_state_possible_subroutes " +
                        " set active = :active where asset_id = :asset_id and route = :route and direction = :direction",
                batchUpdateValues.toArray(new HashMap[batchUpdateValues.size()])
        );

        for (String route : vehicleState.getPossibleRoutes().keySet()) {
            Boolean active = vehicleState.getPossibleRoutes().get(route);
            if (!existsPossibleRoute(vehicleState.getAssetId(), route)) {
                insertPossibleRoute(vehicleState.getAssetId(), route, active);
            } else {
                updatePossibleRoute(vehicleState.getAssetId(), route, active);
            }
        }
    }

    @Override
    public Map<SubrouteKey, Boolean> getPossibleSubroutes(int assetId) {
        String sql = "SELECT * FROM vehicle_state_possible_subroutes WHERE asset_id = ?";
        return jdbcTemplate.query(sql, rs -> {
            Map<SubrouteKey, Boolean> possibleSubroutes = new HashMap<>();
            while (rs.next()) {
                possibleSubroutes.put(new SubrouteKey(rs.getString("route"), rs.getString("direction")), rs.getBoolean("active"));
            }
            return possibleSubroutes;
        }, assetId);
    }

    @Override
    public boolean existsPossibleSubroute(int assetId, SubrouteKey subroute) {
        String sql = "SELECT 1 FROM ref.vehicle_state_possible_subroutes WHERE asset_id = ? AND route = ? and direction = ?";
        List<Object> results = jdbcTemplate.query(sql, new SingleColumnRowMapper<>(), assetId, subroute.getRoute(), subroute.getDirection());
        return (!results.isEmpty());
    }

    @Override
    public void insertPossibleSubroute(int assetId, SubrouteKey subroute, boolean active) {
        possibleSubroutesInsert.execute(createPossibleSubrouteParams(assetId, subroute, active));
    }

    private Map<String, Object> createPossibleSubrouteParams(int assetId, SubrouteKey subroute, boolean active) {
        Map<String, Object> params = new HashMap<>();
        params.put("asset_id", assetId);
        params.put("route", subroute.getRoute());
        params.put("direction", subroute.getDirection());
        params.put("active", active);
        return params;
    }


    @Override
    public void updatePossibleSubroute(int assetId, SubrouteKey subroute, boolean active) {
        UpdateBuilder builder = new UpdateBuilder("vehicle_state_possible_subroutes");
        builder.append("active", active);
        builder.whereEquals("asset_id", assetId);
        builder.whereEquals("route", subroute.getRoute());
        builder.whereEquals("direction", subroute.getDirection());
        jdbcTemplate.update(builder.sql(), builder.values());
    }

    @Override
    public boolean existsPossibleRoute(int assetId, String route) {
        String sql = "SELECT 1 FROM ref.vehicle_state_possible_routes WHERE asset_id = ? AND route = ?";
        List<Object> results = jdbcTemplate.query(sql, new SingleColumnRowMapper<>(), assetId, route);
        return (!results.isEmpty());
    }

    @Override
    public void insertPossibleRoute(int assetId, String route, boolean active) {
        Map<String, Object> params = new HashMap<>();
        params.put("asset_id", assetId);
        params.put("route", route);
        params.put("active", active);
        possibleGeofenceRoutesInsert.execute(params);
    }

    @Override
    public void updatePossibleRoute(int assetId, String route, boolean active) {
        UpdateBuilder builder = new UpdateBuilder("ref.vehicle_state_possible_routes");
        builder.append("active", active);
        builder.whereEquals("asset_id", assetId);
        builder.whereEquals("route", route);
        jdbcTemplate.update(builder.sql(), builder.values());
    }

    @Override
    public Map<String, Boolean> getPossibleRoutes(int assetId) {
        String sql = "SELECT * FROM vehicle_state_possible_routes WHERE asset_id = ?";
        return jdbcTemplate.query(sql, rs -> {
            Map<String, Boolean> possibleRoutes = new HashMap<>();
            while (rs.next()) {
                possibleRoutes.put(rs.getString("route"), rs.getBoolean("active"));
            }
            return possibleRoutes;
        }, assetId);
    }

    private static final VehicleStateMapper VEHICLE_STATE_MAPPER = new VehicleStateMapper();

    private static class VehicleStateMapper implements RowMapper<VehicleState>, ReverseRowMapper<VehicleState> {
        @Override
        public Map<String, Object> reverseMap(VehicleState vehicleState, Action action) {
            Map<String, Object> params = new HashMap<>();
            params.put("last_known_route", vehicleState.getLastKnownRoute());
            params.put("last_known_direction", vehicleState.getLastKnownDirection());
            params.put("within_service_area", vehicleState.isWithinServiceArea());
            params.put("recent_speeds", COMMA_JOINER.join(vehicleState.getRecentSpeeds()));
            params.put("subroute_m", vehicleState.getSubrouteMeasure());
            params.put("avg_speed", vehicleState.getAvgSpeed());
            params.put("within_origin", vehicleState.isWithinOrigin());
            params.put("trip_id", vehicleState.getTripId());
            params.put("stop_gid", vehicleState.getStopId());
            params.put("azimuth", vehicleState.getAzimuth());
            params.put("location_desc", vehicleState.getLocationDescription());
            if(vehicleState.getTrail() != null) {
                params.putAll(TRAIL_MAPPER.reverseMap(vehicleState.getTrail(), action));
            }
            if(vehicleState.getLastTrailChange() != null) {
                params.put("last_trail_change", vehicleState.getLastTrailChange());
            }

            if(action == Action.INSERT) {
                params.put("asset_id", vehicleState.getAssetId());
            }

            return params;
        }

        @Override
        public VehicleState mapRow(ResultSet rs, int rowNum) throws SQLException {
            VehicleState v = new VehicleState();
            v.setAssetId(rs.getInt("asset_id"));
            v.setLastKnownRoute(rs.getString("last_known_route"));
            v.setLastKnownDirection(rs.getString("last_known_direction"));
            v.setWithinServiceArea(rs.getBoolean("within_service_area"));
            v.setAvgSpeed((Float) rs.getObject("avg_speed"));
            v.setWithinOrigin(rs.getBoolean("within_origin"));
            v.setTripId((Long) rs.getObject("trip_id"));
            v.setStopId((Integer) rs.getObject("stop_gid"));
            v.setAzimuth((Float) rs.getObject("azimuth"));
            v.setLocationDescription(rs.getString("location_desc"));
            String recentSpeedsString = rs.getString("recent_speeds");
            if(recentSpeedsString != null) {
                for (String s : COMMA_SPLITTER.split(recentSpeedsString)) {
                    v.getRecentSpeeds().add(Float.valueOf(s));
                }
            }
            if(rs.getObject("trail") != null) {
                v.setTrail(TRAIL_MAPPER.mapRow(rs, rowNum));
            }
            v.setLastTrailChange(rs.getTimestamp("last_trail_change"));
            return v;
        }
    }
}