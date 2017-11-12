package com.testwithspring.intermediate.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan({
        "com.testwithspring.intermediate.message"
})
@Import({
        PersistenceContext.class,
        WebMvcContext.class
})
@PropertySource("classpath:application.properties")
public class ExampleApplicationContext {

}

