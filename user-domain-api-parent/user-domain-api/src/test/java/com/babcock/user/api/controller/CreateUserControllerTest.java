package com.babcock.user.api.controller;

import com.babcock.user.api.Application;
import com.babcock.user.api.application.TestApplication;
import com.babcock.user.api.config.CloudConfiguration;
import com.babcock.user.api.message.MessageChannels;
import com.babcock.user.api.payload.UserDetails;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.stream.annotation.Bindings;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class, webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@EnableBinding(MessageChannels.class)
public class CreateUserControllerTest {

    @Autowired
    MessageCollector messageCollector;

    @Autowired
    MessageChannels messageChannels;

    @LocalServerPort
    private int port;

    private TestRestTemplate testRestTemplate = new TestRestTemplate();

    @Test
    public void createUser() throws Exception {
        String requestJson = "[{" +
                                    "\"username\" : \"testUsername\"," +
                                    "\"firstname\" : \"testUser\"," +
                                    "\"lastname\" : \"testLastname\"" +
                                    "}]";

        String expectedMessage = "{ " +
                "\"username\" : \"testUsername\"," +
                "\"firstname\" : \"testUser\"," +
                "\"lastname\" : \"testLastname\"" +
                "}";

        String plainCreds = "admin:password";
        byte[] plainCredBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredBytes);
        String base64Creds = new String(base64CredsBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Basic " + base64Creds);


        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
        testRestTemplate.postForObject(getHost() + "/createUser/userDetails", entity,String.class);

        Message<String> received = getMessageFromChannel(messageChannels.createChannelOutput());
        assertThat(received.getPayload(), equalTo(expectedMessage));
    }

    @Test
    public void createUser_list() throws Exception {
        String requestJson = "[" +
                    "{" +
                        "\"username\" : \"testUsername\"," +
                        "\"firstname\" : \"testUser\"," +
                        "\"lastname\" : \"testLastname\"" +
                    "}," +
                    "{" +
                        "\"username\" : \"testUsername2\"," +
                        "\"firstname\" : \"testUser2\"," +
                        "\"lastname\" : \"testLastname2\"" +
                    "}" +
                "]";

        String expectedMessage = "{ " +
                "\"username\" : \"testUsername\"," +
                "\"firstname\" : \"testUser\"," +
                "\"lastname\" : \"testLastname\"" +
                "}";

        String expectedMessage2 = "{ " +
                "\"username\" : \"testUsername2\"," +
                "\"firstname\" : \"testUser2\"," +
                "\"lastname\" : \"testLastname2\"" +
                "}";

        String plainCreds = "admin:password";
        byte[] plainCredBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredBytes);
        String base64Creds = new String(base64CredsBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Basic " + base64Creds);


        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
        testRestTemplate.postForObject(getHost() + "/createUser/userDetails", entity,String.class);

        Message<String> received = getMessageFromChannel(messageChannels.createChannelOutput());
        assertThat(received.getPayload(), equalTo(expectedMessage));

        Message<String> received2= getMessageFromChannel(messageChannels.createChannelOutput());
        assertThat(received2.getPayload(), equalTo(expectedMessage2));
    }


    private String getHost() {
        return "http://localhost:" + port + "/user-domain-api";
    }


    private Message<String> getMessageFromChannel(MessageChannel channel) {
        return (Message<String>) messageCollector.forChannel(channel).poll();
    }

}