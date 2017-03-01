package com.testwithspring.intermediate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@ComponentScan("com.testwithspring.intermediate.message")
@EnableWebMvc
public class WebMvcContext extends WebMvcConfigurerAdapter {

    private static final String VIEW_BASE_PATH = "/WEB-INF/jsp/";
    private static final String VIEW_FILENAME_SUFFIX = ".jsp";

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
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
