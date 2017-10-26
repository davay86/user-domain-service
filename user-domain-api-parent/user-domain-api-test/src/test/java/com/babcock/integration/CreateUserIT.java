package com.babcock.integration;

import com.babcock.integration.application.TestApplication;
import com.babcock.integration.asserter.WaitForHelper;
import com.babcock.integration.helper.DatabaseHelper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestApplication.class})
@TestPropertySource("classpath:test.properties")
public class CreateUserIT {

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
    public void createUserAPI_createsUser_and_sendsNotificationEmail() throws InterruptedException {
        String uniqueStr = getUniqueString();

        String requestJson = "["+getExamplePayload(uniqueStr)+"]";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);

        logger.info("sending payload {} to {}",requestJson, userDomainApiUrl + "/createUser/userDetails");
        oAuthRestTemplate.postForObject(userDomainApiUrl + "/createUser/userDetails", entity,String.class);

        logger.info("waiting for mail message to be received...");
        waitForHelper.waitForMailMessages();

        ResponseEntity<String> response = restTemplate.getForEntity(mailServerUrl+"/api/v1/messages", String.class);

        logger.info("assert mail message contents..");
        assertThat(response.getBody(), containsString("To: admin@test.com"));
        assertThat(response.getBody(), containsString("Subject: New User Awaiting Activation"));
        assertThat(response.getBody(), containsString("User joe"+uniqueStr+"(joe "+uniqueStr+") has been created and is awaiting activation"));

        String sql = buildFindByUserNameQuery("joe"+uniqueStr);

        logger.info("waiting for db row from query {}",sql);
        waitForHelper.waitForOneRowInDB(sql);
    }

    public String buildFindByUserNameQuery(String username) {
        return "select count(*) from users where username = '"+username+"'";
    }

    public String getExamplePayload(String uniqueStr) {
        return "{\"username\": \"joe"+uniqueStr+"\",\"firstname\": \"joe\",\"lastname\": \""+uniqueStr+"\"}";
    }


    private String getUniqueString() {
        return RandomStringUtils.randomAlphabetic(10);
    }

}

