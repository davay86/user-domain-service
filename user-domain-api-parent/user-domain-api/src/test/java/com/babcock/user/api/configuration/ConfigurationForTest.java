package com.babcock.user.api.configuration;

import com.babcock.user.api.Application;
import com.babcock.user.api.config.CloudConfiguration;
import com.babcock.user.api.config.SecurityConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;

import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;

@TestConfiguration
@Profile("test")
@ComponentScan(
        basePackages="com.babcock.user.api",
        excludeFilters = {@ComponentScan.Filter(type = ASSIGNABLE_TYPE, value = {
                Application.class,CloudConfiguration.class,SecurityConfiguration.class})
        })
public class ConfigurationForTest {
}
