package com.testwithspring.master.web

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.i18n.FixedLocaleResolver
import java.util.Locale
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

/**
 * This class provides static factory methods that are used
 * to create the Spring MVC infrastructure components that
 * we use when we are writing unit tests for Spring MVC
 * controllers.
 */
class WebTestConfig private constructor () {

    companion object {
        private val LOCALE = Locale.ENGLISH

        /**
         * This method creates a locale resolver that always returns `Locale.ENGLISH`
         * @return
         */
        fun fixedLocaleResolver(): LocaleResolver {
            return FixedLocaleResolver(LOCALE)
        }

        /**
         * Creates a new {@link MappingJackson2HttpMessageConverter} object and configures
         * the used object mapper.
         * @return
         */
        fun objectMapperHttpMessageConverter(): MappingJackson2HttpMessageConverter {
            val converter = MappingJackson2HttpMessageConverter()
            converter.objectMapper = objectMapper()
            return converter
        }

        /**
         * Creates a new {@link ObjectMapper} object.
         * @return
         */
        fun objectMapper(): ObjectMapper {
            return Jackson2ObjectMapperBuilder()
                    .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                    .featuresToEnable(SerializationFeature.WRITE_DATES_WITH_ZONE_ID)
                    .modulesToInstall(
                            Jdk8Module(),
                            JavaTimeModule()
                    )
                    .build()
        }
    }
}