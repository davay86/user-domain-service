package com.babcock.integration;

import com.babcock.integration.application.TestApplication;
import com.babcock.integration.asserter.WaitForHelper;
import com.babcock.integration.helper.DatabaseHelper;
import com.babcock.integration.helper.JsonConverter;
import com.babcock.integration.payload.UserDetails;
import org.apache.commons.lang3.RandomStringUtils;
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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestApplication.class})
@TestPropertySource("classpath:test.properties")
public class ActivateUserIT {

    private static Logger logger = LoggerFactory.getLogger(ActivateUserIT.class);

    @Autowired
    @Qualifier("oauthRestTemplate")
    private RestTemplate oAuthRestTemplate;

    @Autowired
    private WaitForHelper waitForHelper;

    @Autowired
    private JsonConverter jsonConverter;

    @Autowired
    private DatabaseHelper databaseHelper;

    @Value("${user.domain.api.url}")
    String userDomainApiUrl;

    @Value("${mail.server.url}")
    String mailServerUrl;

    String uniqueString;

    String username;
    String activeUsername;

    @Before
    public void before() throws InterruptedException {
        waitForHelper.waitForServices();

        uniqueString = getUniqueString();
        username = "username" + uniqueString;
        activeUsername = "activeUsername" + uniqueString;

        databaseHelper.insertUser(username,"firstname","lastname");
        databaseHelper.insertActiveUser(activeUsername,"activeFirstname", "activeLastname");
    }

    @After
    public void teardown(){
        databaseHelper.clearOutUsers();
    }

    @Test
    public void getPending_returnsUserNotActivated() throws InterruptedException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(headers);


        logger.info("sending request to {}", userDomainApiUrl + "/activateUser/pendingUsers");
        ResponseEntity<String> exchange = oAuthRestTemplate.exchange(userDomainApiUrl + "/activateUser/pendingUsers", HttpMethod.GET, entity, String.class);

        List<UserDetails> userList = jsonConverter.convertjsonStringToUserList(exchange.getBody());

        Assert.assertEquals(1,userList.size());
        UserDetails userDetails = userList.get(0);
        Assert.assertEquals("username" + uniqueString,userDetails.getUsername());
        Assert.assertFalse(userDetails.isActive());
    }

    @Test
    public void activateUser() throws InterruptedException {
        UserDetails userByUsername = databaseHelper.findUserByUsername(username);

        String requestJson = "["+getExamplePayload(userByUsername.getId())+"]";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        logger.info("sending payload {} to {}",requestJson, userDomainApiUrl + "/activateUser/activate");
        oAuthRestTemplate.postForObject(userDomainApiUrl + "/activateUser/activate", entity,String.class);

        String sql = buildFindActiveUserByUserNameQuery(username);

        logger.info("waiting for db row from query {}",sql);
        waitForHelper.waitForOneRowInDB(sql);
    }

    public String buildFindByUserNameQuery(String username) {
        return "select count(*) from users where username = '"+username+"'";
    }

    public String buildFindActiveUserByUserNameQuery(String username) {
        return "select count(*) from users where username = '"+username+"' and active = true";
    }

    public String getExamplePayload(Long id) {
        return "{\"id\": \""+id+"\"}";
    }


    private String getUniqueString() {
        return RandomStringUtils.randomAlphabetic(10);
    }
}
