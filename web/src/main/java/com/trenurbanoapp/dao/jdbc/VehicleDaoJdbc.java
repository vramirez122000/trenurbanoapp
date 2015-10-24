package com.trenurbanoapp.dao.jdbc;

import com.trenurbanoapp.dao.VehicleDao;
import com.trenurbanoapp.model.Vehicle;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 2/16/14
 * Time: 8:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class VehicleDaoJdbc implements VehicleDao {

    private SimpleJdbcInsertOperations vehicleInsert;
    private JdbcOperations jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.vehicleInsert = new SimpleJdbcInsert(dataSource).withTableName("vehicle");
    }

    @Override
    @Cacheable(value = "vehicleCache", key = "#assetId")
    public Vehicle getVehicle(int assetId) {
        List<Vehicle> vehicles = jdbcTemplate.query("select * from ref.vehicle where asset_id = ?", new RowMapper<Vehicle>() {
            @Override
            public Vehicle mapRow(ResultSet rs, int rowNum) throws SQLException {
                Vehicle v = new Vehicle();
                v.setAssetId(rs.getInt("asset_id"));
                v.setName(rs.getString("name"));
                v.setGroupId(rs.getInt("group_id"));
                return v;
            }
        }, assetId);
        return !vehicles.isEmpty() ? vehicles.get(0) : null;
    }

    @Override
    public boolean vehicleExists(int assetId) {
        List<Integer> result = jdbcTemplate.query("select 1 from ref.vehicle where asset_id = ?",
                new SingleColumnRowMapper<>(Integer.class), assetId);
        return !result.isEmpty();
    }

    @Override
    @CacheEvict(value = "vehicleCache", key = "#vehicle.assetId")
    public void updateVehicle(Vehicle vehicle) {
        UpdateBuilder builder = new UpdateBuilder("ref.vehicle");
        builder.appendAll(new VehicleMapper().reverseMap(vehicle, ReverseRowMapper.Action.UPDATE));
        builder.whereEquals("asset_id", vehicle.getAssetId());
        jdbcTemplate.update(builder.sql(), builder.values());
    }

    @Override
    public void insertVehicle(Vehicle vehicle) {
        Map<String, Object> map = new VehicleMapper().reverseMap(vehicle, ReverseRowMapper.Action.INSERT);
        vehicleInsert.execute(map);
    }
}
