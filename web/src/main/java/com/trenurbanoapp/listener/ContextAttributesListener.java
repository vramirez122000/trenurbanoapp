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

        servletContext.setAttribute("app.name", resolver.resolvePlaceholders("${app.name:Tren Urbano App}"));
        servletContext.setAttribute("report.phone", resolver.resolvePlaceholders("${report.phone:311}"));
        servletContext.setAttribute("map.center.lat", resolver.resolvePlaceholders("${map.center.lat:18.430189}"));
        servletContext.setAttribute("map.center.lng", resolver.resolvePlaceholders("${map.center.lng:-66.060061}"));
        servletContext.setAttribute("links.ios", resolver.resolvePlaceholders("${links.ios:http://itunes.apple.com/us/app/tren-urbano-app/id484781635}"));
        servletContext.setAttribute("links.android", resolver.resolvePlaceholders("${links.android:https://market.android.com/details?id=com.trenurbanoapp}"));


        servletContext.setAttribute("logo.16", resolver.resolvePlaceholders("${logo.16:/images/trenurbano_icon16.png}"));
        servletContext.setAttribute("logo.48", resolver.resolvePlaceholders("${logo.48:/images/trenurbano_icon48.png}"));
        servletContext.setAttribute("logo.57", resolver.resolvePlaceholders("${logo.57:/images/trenurbano_icon57.png}"));

        servletContext.setAttribute("banner.231", resolver.resolvePlaceholders("${banner.231:/images/trenurbano_icon_banner231.png}"));
        servletContext.setAttribute("banner.468", resolver.resolvePlaceholders("${banner.468:/images/trenurbano_icon_banner468.png}"));
        servletContext.setAttribute("banner.468", resolver.resolvePlaceholders("${banner.468:/images/trenurbano_icon_banner468.png}"));
        servletContext.setAttribute("content.aboutus", resolver.resolvePlaceholders("${content.aboutus:/html/about_us.html}"));
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
