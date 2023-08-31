package com.app.batch.config;

import org.qlrm.mapper.JpaResultMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Bean
    public JpaResultMapper jpaResultMapper(){
        return new JpaResultMapper();
    }

}