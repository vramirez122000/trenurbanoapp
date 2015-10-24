package com.trenurbanoapp.config;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * User: victor
 * Date: 2/20/14
 */
@Configuration
public class JsonConfig {

    @Bean
    public JsonFactory jsonFactory() {
        JsonFactory jsonFac = new JsonFactory();
        jsonFac.enable(JsonParser.Feature.ALLOW_COMMENTS);
        jsonFac.enable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
        return jsonFac;
    }

    @Bean
    public ObjectMapper objectMapper(JsonFactory jsonFactory) {
        return new ObjectMapper(jsonFactory);
    }
}
