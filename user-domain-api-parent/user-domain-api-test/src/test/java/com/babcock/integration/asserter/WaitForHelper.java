package com.babcock.integration.asserter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.fail;

@Component
@TestPropertySource("classpath:test.properties")
public class WaitForHelper {

    @Autowired
    @Qualifier("restTemplate")
    private RestTemplate restTemplate;

    @Value("${service.url}")
    String baseUrl;

    private static boolean serviceUnavailable = false;

    public void waitForServices() throws InterruptedException {
        WaitForService waitForEmailService = new WaitForService(baseUrl + "/info", restTemplate);
        waitForEmailService.setMaxWaitTime(720000);

        if(serviceUnavailable) {
            fail("user-domain-api docker environment unavailable");
        }

        System.out.println("waiting for service : " + baseUrl + "/info");

        try {
            waitForEmailService.performAssertion();
        }catch (WaitUntilAssertionError wae) {
            serviceUnavailable = true;
            fail("user-domain-api docker environment unavailable");
        }
    }
}
