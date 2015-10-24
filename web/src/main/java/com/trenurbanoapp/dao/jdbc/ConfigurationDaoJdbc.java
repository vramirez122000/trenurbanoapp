package com.trenurbanoapp.dao.jdbc;

import com.trenurbanoapp.dao.ConfigurationDao;
import com.trenurbanoapp.model.Configuration;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by victor on 7/2/14.
 */
public class ConfigurationDaoJdbc implements ConfigurationDao {

    private JdbcOperations jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Configuration getConfiguration() {
        List<Configuration> confs = jdbcTemplate.query("select * from ref.configuration", (rs, rowNum) -> {
            Configuration conf = new Configuration();
            conf.setAssetsHash(rs.getString("assets_hash"));
            return conf;
        });
        if(confs.isEmpty()) {
            throw new IncorrectResultSizeDataAccessException("Configuration table is empty", 1, 0);
        }
        return confs.get(0);
    }

    @Override
    public void saveAssetsHash(String assetsHash) {
        jdbcTemplate.update("update ref.configuration set assets_hash = ?", assetsHash);
    }
}
