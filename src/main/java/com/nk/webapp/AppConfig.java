package com.nk.webapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    MySecondBean mySecondBean() {
        return new MySecondBean("My Property Value");
    }

    @Bean
    public MyBean myBean() {
        return new MyBean();
    }

}
