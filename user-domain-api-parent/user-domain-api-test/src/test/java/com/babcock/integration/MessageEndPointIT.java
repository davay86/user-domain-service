package com.babcock.integration;

import com.babcock.integration.application.TestApplication;
import com.babcock.integration.asserter.WaitForHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestApplication.class})
@TestPropertySource("classpath:test.properties")
public class MessageEndPointIT {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WaitForHelper waitForHelper;

    @Value("${service.url}")
    String baseUrl;

    @Before
    public void before() throws InterruptedException {
        waitForHelper.waitForServices();
    }

    @Test
    public void message_endPoint_returns_asExpected() {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl+"/message/message", String.class);
        assertEquals("Demo Message",response.getBody());
    }
}

