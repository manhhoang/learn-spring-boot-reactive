package com.stream_concurrency.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@ComponentScan("com.stream_concurrency.*")
@PropertySource("classpath:application.properties")
@Configuration
public class AppConfig {

}
