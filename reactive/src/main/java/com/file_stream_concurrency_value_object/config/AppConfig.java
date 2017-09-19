package com.file_stream_concurrency_value_object.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@ComponentScan("com.file_stream_concurrency_value_object.*")
@PropertySource("classpath:application.properties")
@Configuration
public class AppConfig {

}
