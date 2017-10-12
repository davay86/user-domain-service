package com.babcock.integration.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan(value = {"com.babcock.integration"})
@PropertySource("classpath:test.properties")
public class TestConfiguration {

    @Value("${security.auth.id}")
    private String authId;

    @Value("${security.auth.client.id}")
    private String authClientId;

    @Value("${security.auth.client.secret}")
    private String authClientSecret;

    @Value("${security.auth.grant.type}")
    private String grantType;

    @Value("${security.auth.token.url}")
    private String tokenUrl;

    @Value("${database.url}")
    String databaseUrl;

    @Value("${database.driver}")
    String databaseDriver;

    @Value("${database.user}")
    String databaseUser;

    @Value("${database.password}")
    String databasePassword;

    @Bean
    public JdbcTemplate jdbcTemplate() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(databaseDriver);
        dataSource.setUrl(databaseUrl);
        dataSource.setUsername(databaseUser);
        dataSource.setPassword(databasePassword);

        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "oauthRestTemplate")
    public RestTemplate oauthRestTemplate() {
        ClientCredentialsResourceDetails resourceDetails = new ClientCredentialsResourceDetails();

        resourceDetails.setId(authId);
        resourceDetails.setClientId(authClientId);
        resourceDetails.setClientSecret(authClientSecret);
        resourceDetails.setAccessTokenUri(tokenUrl);
        resourceDetails.setGrantType(grantType);

        DefaultOAuth2ClientContext context = new DefaultOAuth2ClientContext();
        return new OAuth2RestTemplate(resourceDetails,context);
    }

    @Bean(name = "restTemplate")
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}