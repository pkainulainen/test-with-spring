package com.testwithspring.intermediate.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Import({
        WebMvcContext.class
})
public class ExampleApplicationContext {
}
