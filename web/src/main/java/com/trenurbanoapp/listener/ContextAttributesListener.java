package com.trenurbanoapp.listener;

import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by victor on 4/7/14.
 */
public class ContextAttributesListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent contextEvent) {
        ServletContext servletContext = contextEvent.getServletContext();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        PropertySourcesPlaceholderConfigurer bean = webApplicationContext.getBean(PropertySourcesPlaceholderConfigurer.class);
        PropertySourcesPropertyResolver resolver = new PropertySourcesPropertyResolver(bean.getAppliedPropertySources());
        servletContext.setAttribute("gps.enabled", Boolean.valueOf(resolver.resolvePlaceholders("${gps.enabled:false}")));
        servletContext.setAttribute("appCache.disabled", Boolean.valueOf(resolver.resolvePlaceholders("${appCache.disabled:false}")));
        servletContext.setAttribute("javascript.useSource", Boolean.valueOf(resolver.resolvePlaceholders("${javascript.useSource:false}")));
        servletContext.setAttribute("routeAlgorithm.useRoutes", Boolean.valueOf(resolver.resolvePlaceholders("${routeAlgorithm.useRoutes:false}")));
        servletContext.setAttribute("alert.message", resolver.resolvePlaceholders("${alert.message:}"));
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
