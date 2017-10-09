package com.babcock.user.api.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan("com.babcock.user.api")
@Import({SecurityConfiguration.class,SwaggerConfiguration.class})
public class CloudConfiguration {
}
