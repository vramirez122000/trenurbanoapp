package com.trenurbanoapp.dao;

import com.trenurbanoapp.model.Configuration;

/**
 * Created by victor on 7/2/14.
 */
public interface ConfigurationDao {
    Configuration getConfiguration();

    void saveAssetsHash(String assetsHash);
}
