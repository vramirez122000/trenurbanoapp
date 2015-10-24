package com.trenurbanoapp.dao.jdbc;

import com.trenurbanoapp.model.Geofence;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
* Created with IntelliJ IDEA.
* User: victor
* Date: 5/24/13
* Time: 10:33 PM
* To change this template use File | Settings | File Templates.
*/
class GeofenceMapper implements RowMapper<Geofence>, ReverseRowMapper<Geofence> {

    private PolygonMapper pathMapper = new PolygonMapper("geom");

    @Override
    public Geofence mapRow(ResultSet rs, int rowNum) throws SQLException {
        Geofence geofence = new Geofence();
        geofence.setId(rs.getInt("gid"));
        geofence.setAvlId((Integer) rs.getObject("avl_id"));
        geofence.setType(Geofence.Type.valueOf(rs.getString("type")));
        geofence.setDescription(rs.getString("name"));
        return geofence;
    }

    @Override
    public Map<String, Object> reverseMap(Geofence geofence, Action action) {
        Map<String, Object> params = new HashMap<>();
        params.put("avl_id", geofence.getAvlId());
        params.put("type", geofence.getType().name());
        params.put("name", geofence.getDescription());
        params.put("geom", pathMapper.reverseMap(geofence.getPath(), action));
        return params;
    }
}
