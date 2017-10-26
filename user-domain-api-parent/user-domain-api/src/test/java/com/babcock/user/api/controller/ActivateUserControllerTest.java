package com.babcock.user.api.controller;


import com.babcock.user.api.application.TestApplication;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class, webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ActivateUserControllerTest {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ActivateUserController activateUserController;

    MockRestServiceServer server;

    @Before
    public void setup(){
        server = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void getPendingUsers(){
        String returnString = "returnString";
        server.expect(requestTo("http://localhost/user-service/activateUser/getPending"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(returnString, MediaType.TEXT_PLAIN));

        String pendingUsers = activateUserController.getPendingUsers();

        server.verify();

        Assert.assertEquals(returnString,pendingUsers);
    }
}