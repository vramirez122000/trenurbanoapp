package com.trenurbanoapp.dao.jdbc;

import com.trenurbanoapp.dao.GeofenceDao;
import com.trenurbanoapp.model.Geofence;
import com.trenurbanoapp.scraper.model.LatLng;
import org.postgis.PGgeometry;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by victor on 3/2/14.
 */
public class GeofenceDaoJdbc implements GeofenceDao {

    private JdbcOperations jdbcTemplate;
    private SimpleJdbcInsert geofenceInsert;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.geofenceInsert = new SimpleJdbcInsert(dataSource)
                .withSchemaName("ref")
                .withTableName("geofence")
                .usingGeneratedKeyColumns("gid");
    }

    @Override
    public void insertGeofence(Geofence geofence) {
        geofenceInsert.execute(Mappers.GEOFENCE_MAPPER.reverseMap(geofence, ReverseRowMapper.Action.INSERT));
    }

    @Override
    public Geofence getGeofenceByAvlId(String avlId) {
        String sql = "SELECT * FROM ref.geofence where avl_id = ?";
        List<Geofence> results = jdbcTemplate.query(sql, Mappers.GEOFENCE_MAPPER, avlId);
        return !results.isEmpty() ? results.get(0): null;
    }

    @Override
    @Cacheable(value = "geofenceCache")
    public Geofence getGeofenceById(Integer id) {
        String sql = "SELECT * FROM ref.geofence where gid = ?";
        List<Geofence> results = jdbcTemplate.query(sql, Mappers.GEOFENCE_MAPPER, id);
        return !results.isEmpty() ? results.get(0): null;
    }

    @Override
    public List<Geofence> getContainingGeofences(List<LatLng> line, Integer... subset) {
        CriteriaQueryBuilder builder = new CriteriaQueryBuilder("select * from ref.geofence");
        if(subset != null && subset.length > 0) {
            builder.whereIn("gid", (Object[]) subset);
        }
        PGgeometry geom = Mappers.toPGGeometry(line);
        builder.whereClause("ST_Contains(geom, ?)", geom);
        return jdbcTemplate.query(builder.sql(), Mappers.GEOFENCE_MAPPER, builder.values());
    }

    @Override
    public List<Geofence> getContainingGeofencesByType(List<LatLng> line, Geofence.Type type) {
        CriteriaQueryBuilder builder = new CriteriaQueryBuilder("select * from ref.geofence");
        builder.whereEquals("type", type.name());
        PGgeometry geom = Mappers.toPGGeometry(line);
        builder.whereClause("ST_Contains(geom, ?)", geom);
        return jdbcTemplate.query(builder.sql(), Mappers.GEOFENCE_MAPPER, builder.values());
    }

    @Override
    public boolean isWithinGeofenceByType(LatLng latLng, Geofence.Type type) {
        CriteriaQueryBuilder builder = new CriteriaQueryBuilder("select 1 from ref.geofence");
        builder.whereEquals("type", type.name());
        builder.whereClause("ST_Contains(geom, ?)", new PGgeometry(Mappers.toPoint(latLng)));
        List<Integer> result = jdbcTemplate.query(builder.sql(), new SingleColumnRowMapper<>(Integer.class), builder.values());
        return !result.isEmpty();
    }

    @Override
    public boolean isWithinGeofence(List<LatLng> line, int geofenceId) {
        CriteriaQueryBuilder builder = new CriteriaQueryBuilder("select 1 from ref.geofence");
        builder.whereEquals("gid", geofenceId);
        PGgeometry geom = Mappers.toPGGeometry(line);
        builder.whereClause("ST_Contains(geom, ?)", geom);
        List<Integer> result = jdbcTemplate.query(builder.sql(), new SingleColumnRowMapper<>(Integer.class), builder.values());
        return !result.isEmpty();
    }

}
