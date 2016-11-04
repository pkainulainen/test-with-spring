package com.testwithspring.starter.springboot.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.Locale;

/**
 * This class provides static factory methods that are used
 * to create the Spring MVC infrastructure components that
 * we use when we are writing unit tests for Spring MVC
 * controllers.
 */
public final class WebTestConfig {

    public static final Locale LOCALE = Locale.ENGLISH;

    /**
     * Prevents instantiation.
     */
    private WebTestConfig() {}

    /**
     *
     * @return
     */
    public static MappingJackson2HttpMessageConverter objectMapperHttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper());
        return converter;
    }

    public static ObjectMapper objectMapper() {
        return new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .registerModule(new Jdk8Module());
    }
}
