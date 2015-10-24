package com.trenurbanoapp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
public class PropertyConfig {


    private static Logger logger = LoggerFactory.getLogger(PropertyConfig.class);

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer(Environment environment) throws IOException {

        // from command-line
        if (environment.getProperty("config.property.file") != null) {
            final Resource commandLinePropertyFile = new FileSystemResource(environment.getProperty("config.property.file"));
            if (commandLinePropertyFile.exists()) {
                return propertySource(commandLinePropertyFile);
            }
        }

        throw new IllegalArgumentException("no config property file found!");

    }

    private static PropertySourcesPlaceholderConfigurer propertySource( Resource propertyResource) throws IOException {
        PropertySourcesPlaceholderConfigurer props = new PropertySourcesPlaceholderConfigurer();
        logger.debug("Running with config.property.file="+propertyResource.getFile().getAbsolutePath());
        props.setLocation(propertyResource);
        return props;
    }

}