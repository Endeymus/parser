package com.endeymus.parser.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("com.endeymus.parser")
@PropertySource("classpath:properties/weather.properties")
public class SpringConfig {
}
