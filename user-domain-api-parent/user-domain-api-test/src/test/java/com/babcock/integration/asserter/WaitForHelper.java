package com.babcock.integration.asserter;

import com.noveria.assertion.exception.WaitUntilAssertionError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.fail;

@Component
@TestPropertySource("classpath:test.properties")
public class WaitForHelper {

    private static Logger logger = LoggerFactory.getLogger(WaitForHelper.class);

    @Autowired
    @Qualifier("restTemplate")
    private RestTemplate restTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${user.domain.api.url}")
    String userDomainApiUrl;

    @Value("${user.service.url}")
    String userServiceUrl;

    @Value("${email.service.url}")
    String emailServiceUrl;

    @Value("${mail.server.url}")
    String mailServerUrl;

    private static boolean serviceUnavailable = false;

    public void waitForServices() throws InterruptedException {
        waitForService(emailServiceUrl);
        waitForService(userServiceUrl);
        waitForService(userDomainApiUrl);
    }

    private void waitForService(String serviceUrl) throws InterruptedException {
        WaitForService waitForService = new WaitForService(serviceUrl + "/info", restTemplate);
        waitForService.setMaxWaitTime(720000);

        if(serviceUnavailable) {
            fail("service url "+serviceUrl+" unavailable");
        }

        logger.info("waiting for service url {}", serviceUrl + "/info");

        try {
            waitForService.performAssertion();
        }catch (WaitUntilAssertionError wae) {
            serviceUnavailable = true;
            fail("service url "+serviceUrl+" unavailable");
        }
    }

    public void waitForMailMessages() throws InterruptedException {
        WaitForMail waitForMail = new WaitForMail(mailServerUrl+"/api/v1/messages", restTemplate);
        waitForMail.setMaxWaitTime(60000);
        waitForMail.performAssertion();
    }

    public void waitForOneRowInDB(String query) throws InterruptedException {
        WaitForOneRowInDB waitForOneRowInDB = new WaitForOneRowInDB(query, jdbcTemplate);
        waitForOneRowInDB.setMaxWaitTime(10000);
        waitForOneRowInDB.performAssertion();
    }
}
