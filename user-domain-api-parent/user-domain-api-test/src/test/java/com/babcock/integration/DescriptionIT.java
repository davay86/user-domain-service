package com.babcock.integration;

import com.babcock.integration.application.TestApplication;
import com.babcock.integration.asserter.WaitForHelper;
import com.babcock.integration.helper.DatabaseHelper;
import com.babcock.integration.payload.UserDetails;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestApplication.class})
@TestPropertySource("classpath:test.properties")
public class DescriptionIT {

    private static Logger logger = LoggerFactory.getLogger(CreateUserIT.class);

    @Autowired
    @Qualifier("oauthRestTemplate")
    private RestTemplate oAuthRestTemplate;

    @Autowired
    @Qualifier("restTemplate")
    private RestTemplate restTemplate;

    @Autowired
    private WaitForHelper waitForHelper;

    @Autowired
    private DatabaseHelper databaseHelper;

    @Value("${user.domain.api.url}")
    String userDomainApiUrl;

    @Value("${mail.server.url}")
    String mailServerUrl;

    @Before
    public void before() throws InterruptedException {
        waitForHelper.waitForServices();
        restTemplate.delete(mailServerUrl+"/api/v1/messages", String.class);
    }

    @After
    public void teardown(){
        databaseHelper.clearOutUsers();
    }

    @Test
    public void getDescription_returnsDescriptionMessage() throws InterruptedException {
        String returnString = "This is the user-domain-api microservice";

        logger.info("sending request to {}", userDomainApiUrl + "/description/message");

        String description = oAuthRestTemplate.getForObject(userDomainApiUrl + "/description/message", String.class);
        Assert.assertEquals(returnString,description);
    }
}
