package com.trenurbanoapp.dao.jdbc;

import com.trenurbanoapp.model.Vehicle;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 2/16/14
 * Time: 8:40 PM
 * To change this template use File | Settings | File Templates.
 */
class VehicleMapper implements ReverseRowMapper<Vehicle>, RowMapper<Vehicle> {

    private String prefix = "";

    public VehicleMapper() {
    }

    public VehicleMapper(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public Map<String, Object> reverseMap(Vehicle v, Action action) {
        if(action == null) {
            throw new IllegalArgumentException("action cannot be null");
        }

        Map<String, Object> map = new HashMap<>();
        if(action == Action.INSERT) {
            map.put("asset_id", v.getAssetId());
        }
        map.put("group_id", v.getGroupId());
        map.put("name", v.getName());
        map.put("routes", v.getRoutes());
        return map;
    }

    @Override
    public Vehicle mapRow(ResultSet rs, int i) throws SQLException {
        Vehicle v = new Vehicle();
        v.setAssetId(rs.getInt(prefix + "asset_id"));
        v.setName(rs.getString(prefix + "name"));
        v.setGroupId(rs.getInt(prefix + "group_id"));
        Array routes = rs.getArray(prefix + "routes");
        if(routes != null) {
            v.setRoutes((String[]) routes.getArray());
        }
        return v;
    }
}
