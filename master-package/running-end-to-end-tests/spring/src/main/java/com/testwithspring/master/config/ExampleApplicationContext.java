package com.testwithspring.master.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        WebMvcContext.class
})
public class ExampleApplicationContext {
}
