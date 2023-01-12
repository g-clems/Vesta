package com.augery.vesta.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = "com.augery.vesta")
@EnableJpaRepositories
public class GlobalConfiguration {

}
