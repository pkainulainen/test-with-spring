package com.testwithspring.intermediate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import java.util.Locale;
import java.util.Properties;

@Configuration
@ComponentScan("com.testwithspring.intermediate.web")
@EnableWebMvc
public class WebMvcContext extends WebMvcConfigurerAdapter {

    private static final String HTTP_STATUS_CODE_INTERNAL_SERVER_ERROR = "500";
    private static final String HTTP_STATUS_CODE_NOT_FOUND = "404";

    public static final Locale LOCALE = Locale.ENGLISH;

    private static final String VIEW_BASE_PATH = "/WEB-INF/jsp/";
    private static final String VIEW_FILENAME_SUFFIX = ".jsp";
    private static final String VIEW_NAME_ERROR_VIEW = "error/error";
    private static final String VIEW_NAME_NOT_FOUND_VIEW = "error/404";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    SimpleMappingExceptionResolver exceptionResolver() {
        SimpleMappingExceptionResolver exceptionResolver = new SimpleMappingExceptionResolver();

        Properties exceptionMappings = new Properties();

        exceptionMappings.put("com.testwithspring.intermediate.common.NotFoundException", VIEW_NAME_NOT_FOUND_VIEW);
        exceptionMappings.put("java.lang.Exception", VIEW_NAME_ERROR_VIEW);
        exceptionMappings.put("java.lang.RuntimeException", VIEW_NAME_ERROR_VIEW);

        exceptionResolver.setExceptionMappings(exceptionMappings);

        Properties statusCodes = new Properties();

        statusCodes.put(VIEW_NAME_NOT_FOUND_VIEW, HTTP_STATUS_CODE_NOT_FOUND);
        statusCodes.put(VIEW_NAME_ERROR_VIEW, HTTP_STATUS_CODE_INTERNAL_SERVER_ERROR);

        exceptionResolver.setStatusCodes(statusCodes);

        return exceptionResolver;
    }

    @Profile(Profiles.INTEGRATION_TEST)
    @Bean
    LocaleResolver fixedLocaleResolver() {
        return new FixedLocaleResolver(LOCALE);
    }

    @Profile(Profiles.APPLICATION)
    @Bean
    LocaleResolver localeResolver() {
        return new CookieLocaleResolver();
    }

    @Bean
    ViewResolver jspViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();

        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix(VIEW_BASE_PATH);
        viewResolver.setSuffix(VIEW_FILENAME_SUFFIX);

        return viewResolver;
    }
}
