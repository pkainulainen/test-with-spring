package com.testwithspring.master.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.i18n.FixedLocaleResolver

/**
 * This class provides static factory methods that are used
 * to create the Spring MVC infrastructure components that
 * we use when we are writing unit tests for Spring MVC
 * controllers.
 */
final class WebTestConfig {

    static final Locale LOCALE = Locale.ENGLISH

    /**
     * This method creates a locale resolver that always returns {@code Locale.ENGLISH}.
     * @return
     */
    def static fixedLocaleResolver() {
        return new FixedLocaleResolver(LOCALE)
    }

    /**
     * Creates a new {@link MappingJackson2HttpMessageConverter} object and configures
     * the used object mapper.
     * @return
     */
    def static objectMapperHttpMessageConverter() {
        return new MappingJackson2HttpMessageConverter(objectMapper: objectMapper())
    }

    /**
     * Creates a new {@link ObjectMapper} object.
     * @return
     */
    def static objectMapper() {
        new Jackson2ObjectMapperBuilder()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .featuresToEnable(SerializationFeature.WRITE_DATES_WITH_ZONE_ID)
                .modulesToInstall(
                        new Jdk8Module(),
                        new JavaTimeModule()
                )
                .build()
    }
}
