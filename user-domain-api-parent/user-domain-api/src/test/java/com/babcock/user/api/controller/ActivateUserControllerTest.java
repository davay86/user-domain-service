package com.babcock.user.api.controller;


import com.babcock.user.api.application.TestApplication;
import com.babcock.user.api.message.MessageChannels;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.http.*;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class, webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@EnableBinding(MessageChannels.class)
public class ActivateUserControllerTest {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ActivateUserController activateUserController;

    @Autowired
    MessageChannels messageChannels;

    @Autowired
    MessageCollector messageCollector;

    @LocalServerPort
    private int port;

    MockRestServiceServer server;

    private TestRestTemplate testRestTemplate = new TestRestTemplate();

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

    @Test
    public void activateUser() throws Exception {
        String requestJson = "[{\"id\":\"123\"}]";

        String expectedMessage = "{\"id\":\"123\"}";

        String plainCreds = "admin:password";
        byte[] plainCredBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredBytes);
        String base64Creds = new String(base64CredsBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Basic " + base64Creds);


        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
        testRestTemplate.postForObject(getHost() + "/activateUser/activate", entity,String.class);

        Message<String> received = getMessageFromChannel(messageChannels.activateUserChannelOutput());
        assertThat(received.getPayload(), equalTo(expectedMessage));
    }

    private Message<String> getMessageFromChannel(MessageChannel channel) {
        return (Message<String>) messageCollector.forChannel(channel).poll();
    }

    private String getHost() {
        return "http://localhost:" + port + "/user-domain-api";
    }
}