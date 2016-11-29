package com.testwithspring.intermediate.message;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class MessageApplication {

    @Profile(Profiles.APPLICATION)
    @Bean
    MessageService appMessageService() {
        return new MessageService("Hello World!");
    }

    @Profile(Profiles.INTEGRATION_TEST)
    @Bean
    MessageService testMessageService() {
        return new MessageService("Hello Test!");
    }

    public static void main(String[] args) {
        SpringApplication.run(MessageApplication.class, args);
    }
}
