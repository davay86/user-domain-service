package com.babcock.user.api.configuration;

import com.babcock.user.api.Application;
import com.babcock.user.api.config.CloudConfiguration;
import com.babcock.user.api.config.SecurityConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;
import static org.springframework.context.annotation.FilterType.REGEX;

@TestConfiguration
@Profile("test")
@ComponentScan(
        basePackages="com.babcock.user.api",
        excludeFilters = {@ComponentScan.Filter(type = ASSIGNABLE_TYPE, value = {
                Application.class,CloudConfiguration.class,SecurityConfiguration.class})
        , @ComponentScan.Filter(type = REGEX, pattern = "com.babcock.user.api.*Test")})
public class ConfigurationForTest {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
