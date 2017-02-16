package com.testwithspring.intermediate.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

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
     * This method creates a locale resolver that always returns {@code Locale.ENGLISH}
     * @return
     */
    public static LocaleResolver fixedLocaleResolver() {
        return new FixedLocaleResolver(LOCALE);
    }

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
