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
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.util.List;

/**
 * Created by victor on 3/2/14.
 */
@Repository
public class GeofenceDaoJdbc implements GeofenceDao {

    private JdbcOperations jdbcTemplate;

    @Inject
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public boolean isWithinGeofenceByType(LatLng latLng, Geofence.Type type) {
        CriteriaQueryBuilder builder = new CriteriaQueryBuilder("select 1 from ref.geofence");
        builder.whereEquals("type", type.name());
        builder.whereClause("ST_Contains(geom, ?)", new PGgeometry(Mappers.toPoint(latLng)));
        List<Integer> result = jdbcTemplate.query(builder.sql(), new SingleColumnRowMapper<>(Integer.class), builder.values());
        return !result.isEmpty();
    }



}
