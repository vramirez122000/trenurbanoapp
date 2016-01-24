package com.trenurbanoapp.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by victor on 24/01/16.
 */
public class BeforeSpringListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.setProperty("log4jdbc.drivers", "org.postgis.DriverWrapper");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
