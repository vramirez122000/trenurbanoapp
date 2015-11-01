package com.trenurbanoapp.dao.jdbc;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.trenurbanoapp.dao.VehicleStateDao;
import com.trenurbanoapp.model.VehicleState;
import java.time.LocalDateTime;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * User: victor
 * Date: 5/11/13
 * Time: 8:59 AM
 */
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
                .withTableName("vehicle_state_possible_geofence_routes");
    }



    @Override
    public VehicleState getVehicleState(final int assetId) {
        String sql = "SELECT * FROM ref.vehicle_state WHERE asset_id = ?";
        List<VehicleState> results = jdbcTemplate.query(sql, VEHICLE_STATE_MAPPER, assetId);

        if (results.isEmpty()) {
            return null;
        }

        VehicleState v = results.get(0);
        v.setPossibleSubrouteIds(getPossibleSubroutes(assetId));
        v.setPossibleGeofenceRouteIds(getPossibleGeofenceRoutes(assetId));
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

        for (Integer subrouteId : vehicleState.getPossibleSubrouteIds().keySet()) {
            insertPossibleSubroute(
                    vehicleState.getAssetId(),
                    subrouteId,
                    vehicleState.getPossibleSubrouteIds().get(subrouteId)
            );
        }

        for (Integer geofenceId : vehicleState.getPossibleGeofenceRouteIds().keySet()) {
            insertPossibleGeofenceRoute(
                    vehicleState.getAssetId(),
                    geofenceId,
                    vehicleState.getPossibleGeofenceRouteIds().get(geofenceId)
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
        UpdateBuilder builder = new UpdateBuilder("ref.vehicle_state");
        builder.appendAll(VEHICLE_STATE_MAPPER.reverseMap(vehicleState, ReverseRowMapper.Action.UPDATE));
        builder.whereEquals("asset_id", vehicleState.getAssetId());
        jdbcTemplate.update(builder.sql(), builder.values());

        List<Map<String, ?>> batchUpdateValues = new ArrayList<>();
        for (Integer subrouteId : vehicleState.getPossibleSubrouteIds().keySet()) {
            Boolean active = vehicleState.getPossibleSubrouteIds().get(subrouteId);
            String cacheKey = String.format("%s%s", vehicleState.getAssetId(), subrouteId);
            if (!possibleRouteExistsCache.contains(cacheKey)) {
                possibleRouteExistsCache.add(cacheKey);
                if(!existsPossibleSubroute(vehicleState.getAssetId(), subrouteId)) {
                    insertPossibleSubroute(vehicleState.getAssetId(), subrouteId, active);
                } else {
                    batchUpdateValues.add(createPossibleSubrouteParams(vehicleState.getAssetId(), subrouteId, active));
                }
            } else {
                batchUpdateValues.add(createPossibleSubrouteParams(vehicleState.getAssetId(), subrouteId, active));
            }
        }

        namedParameterJdbcTemplate.batchUpdate("update ref.vehicle_state_possible_subroutes " +
                        " set active = :active where asset_id = :asset_id and subroute_gid = :subroute_gid",
                batchUpdateValues.toArray(new HashMap[batchUpdateValues.size()])
        );

        for (Integer geofenceId : vehicleState.getPossibleGeofenceRouteIds().keySet()) {
            Boolean active = vehicleState.getPossibleGeofenceRouteIds().get(geofenceId);
            if (!existsPossibleGeofenceRoute(vehicleState.getAssetId(), geofenceId)) {
                insertPossibleGeofenceRoute(vehicleState.getAssetId(), geofenceId, active);
            } else {
                updatePossibleGeofenceRoute(vehicleState.getAssetId(), geofenceId, active);
            }
        }
    }

    @Override
    public Map<Integer, Boolean> getPossibleSubroutes(int assetId) {
        String sql = "SELECT * FROM ref.vehicle_state_possible_subroutes WHERE asset_id = ?";
        return jdbcTemplate.query(sql, new ResultSetExtractor<Map<Integer, Boolean>>() {
            @Override
            public Map<Integer, Boolean> extractData(ResultSet rs) throws SQLException, DataAccessException {
                Map<Integer, Boolean> possibleSubroutes = new HashMap<>();
                while (rs.next()) {
                    possibleSubroutes.put((Integer) rs.getObject("subroute_gid"), rs.getBoolean("active"));
                }
                return possibleSubroutes;
            }
        }, assetId);
    }

    @Override
    public List<String> getPossibleSubroutesNames(int assetId) {
        String sql = "SELECT subroute.name FROM " +
                " ref.vehicle_state_possible_subroutes as vehicle_state_possible_subroutes " +
                " join ref.subroute as subroute on vehicle_state_possible_subroutes.subroute_gid = subroute.gid " +
                " WHERE vehicle_state_possible_subroutes.asset_id = ? " +
                " and vehicle_state_possible_subroutes.active = true ";
        return jdbcTemplate.query(sql, new SingleColumnRowMapper<>(String.class), assetId);
    }

    @Override
    public boolean existsPossibleSubroute(int assetId, int subrouteId) {
        String sql = "SELECT 1 FROM ref.vehicle_state_possible_subroutes WHERE asset_id = ? AND subroute_gid = ?";
        List<Object> results = jdbcTemplate.query(sql, new SingleColumnRowMapper<>(), assetId, subrouteId);
        return (!results.isEmpty());
    }

    @Override
    public void insertPossibleSubroute(int assetId, int subrouteId, boolean active) {
        possibleSubroutesInsert.execute(createPossibleSubrouteParams(assetId, subrouteId, active));
    }

    private Map<String, Object> createPossibleSubrouteParams(int assetId, int subrouteId, boolean active) {
        Map<String, Object> params = new HashMap<>();
        params.put("asset_id", assetId);
        params.put("subroute_gid", subrouteId);
        params.put("active", active);
        return params;
    }


    @Override
    public void updatePossibleSubroute(int assetId, int subrouteId, boolean active) {
        UpdateBuilder builder = new UpdateBuilder("ref.vehicle_state_possible_subroutes");
        builder.append("active", active);
        builder.whereEquals("asset_id", assetId);
        builder.whereEquals("subroute_gid", subrouteId);
        jdbcTemplate.update(builder.sql(), builder.values());
    }

    @Override
    public List<String> getPossibleGeofenceRoutesNames(int assetId) {
        String sql = "SELECT geofence.name " +
                " FROM ref.vehicle_state_possible_geofence_routes as vehicle_state_possible_geofence_routes " +
                " join ref.geofence as geofence on vehicle_state_possible_geofence_routes.geofence_gid = geofence.gid " +
                " WHERE vehicle_state_possible_geofence_routes.asset_id = ? " +
                " and vehicle_state_possible_geofence_routes.active = true ";
        return jdbcTemplate.query(sql, new SingleColumnRowMapper<>(String.class), assetId);
    }

    @Override
    public boolean existsPossibleGeofenceRoute(int assetId, int geofenceId) {
        String sql = "SELECT 1 FROM ref.vehicle_state_possible_geofence_routes WHERE asset_id = ? AND geofence_gid = ?";
        List<Object> results = jdbcTemplate.query(sql, new SingleColumnRowMapper<>(), assetId, geofenceId);
        return (!results.isEmpty());
    }

    @Override
    public void insertPossibleGeofenceRoute(int assetId, int geofenceId, boolean active) {
        Map<String, Object> params = new HashMap<>();
        params.put("asset_id", assetId);
        params.put("geofence_gid", geofenceId);
        params.put("active", active);
        possibleGeofenceRoutesInsert.execute(params);
    }

    @Override
    public void updatePossibleGeofenceRoute(int assetId, int geofenceId, boolean active) {
        UpdateBuilder builder = new UpdateBuilder("ref.vehicle_state_possible_geofence_routes");
        builder.append("active", active);
        builder.whereEquals("asset_id", assetId);
        builder.whereEquals("geofence_gid", geofenceId);
        jdbcTemplate.update(builder.sql(), builder.values());
    }

    @Override
    public Map<Integer, Boolean> getPossibleGeofenceRoutes(int assetId) {
        String sql = "SELECT * FROM ref.vehicle_state_possible_geofence_routes WHERE asset_id = ?";
        return jdbcTemplate.query(sql, rs -> {
            Map<Integer, Boolean> possibleGeofenceRoutes = new HashMap<>();
            while (rs.next()) {
                possibleGeofenceRoutes.put((Integer) rs.getObject("geofence_gid"), rs.getBoolean("active"));
            }
            return possibleGeofenceRoutes;
        }, assetId);
    }

    private static final VehicleStateMapper VEHICLE_STATE_MAPPER = new VehicleStateMapper();

    private static class VehicleStateMapper implements RowMapper<VehicleState>, ReverseRowMapper<VehicleState> {
        @Override
        public Map<String, Object> reverseMap(VehicleState vehicleState, Action action) {
            Map<String, Object> params = new HashMap<>();
            params.put("last_known_route_geofence_id", vehicleState.getLastKnownRouteGeofenceId());
            params.put("last_known_subroute_id", vehicleState.getLastKnownSubrouteId());
            params.put("within_service_area", vehicleState.isWithinServiceArea());
            params.put("recent_speeds", COMMA_JOINER.join(vehicleState.getRecentSpeeds()));
            params.put("subroute_m", vehicleState.getSubrouteMeasure());
            params.put("avg_speed", vehicleState.getAvgSpeed());
            params.put("within_origin", vehicleState.isWithinOrigin());
            params.put("trip_id", vehicleState.getTripId());
            params.put("stop_gid", vehicleState.getStopId());
            params.put("location_desc", vehicleState.getLocationDescription());
            params.put("cardinal_dir", vehicleState.getCardinalDirection());
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
            v.setLastKnownRouteGeofenceId((Integer) rs.getObject("last_known_route_geofence_id"));
            v.setLastKnownSubrouteId((Integer) rs.getObject("last_known_subroute_id"));
            v.setWithinServiceArea(rs.getBoolean("within_service_area"));
            v.setAvgSpeed((Float) rs.getObject("avg_speed"));
            v.setWithinOrigin(rs.getBoolean("within_origin"));
            v.setTripId((Long) rs.getObject("trip_id"));
            v.setStopId((Integer) rs.getObject("stop_gid"));
            v.setLocationDescription(rs.getString("location_desc"));
            v.setCardinalDirection(rs.getString("cardinal_dir"));
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