package com.trenurbanoapp.editor;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * Created by victor on 11/11/15.
 */
@Repository
public class SubrouteEditorDao {

    private JdbcOperations jdbcTemplate;

    @Inject
    public SubrouteEditorDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Map<String, Object>> getSubroutesByRouteName(String routeName) {
        return jdbcTemplate.queryForList("SELECT subroute.gid, subroute.name, subroute.dest, route.color, " +
                " st_asgeojson(st_transform(subroute.geom, 4326)) geojson " +
                " FROM subroute JOIN route ON subroute.name = route.name " +
                " WHERE subroute.name = ? ", routeName);
    }

    public List<Map<String, Object>> getSubroutesForEdit() {
        return jdbcTemplate.queryForList("SELECT subroute.gid, subroute.name, subroute.dest, route.color, " +
                " st_asgeojson(st_transform(subroute.geom, 4326)) geojson " +
                " FROM subroute JOIN route ON subroute.name = route.name ");
    }
}
