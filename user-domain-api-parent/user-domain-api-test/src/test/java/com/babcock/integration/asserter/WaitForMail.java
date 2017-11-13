package com.babcock.integration.asserter;

import com.noveria.assertion.asserter.WaitUntilAsserter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

public class WaitForMail extends WaitUntilAsserter {

    private static Logger logger = LoggerFactory.getLogger(WaitForMail.class);

    private String url;
    private RestTemplate restTemplate;

    public WaitForMail(String url, RestTemplate restTemplate) {
        super();
        this.url = url;
        this.restTemplate = restTemplate;
    }

    @Override
    protected boolean execute() {
        try {

            logger.info("waiting for mail service at url : {}",url);
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if(!response.getBody().isEmpty() && !response.getBody().equals("[]")) {
                return true;
            }else {
                return false;
            }

        }catch (ResourceAccessException ex) {
            return false;
        }catch (Exception ex) {
            logger.error(ex.getMessage(),ex);
            return false;
        }
    }

    @Override
    protected String getTaskName() {
        return "WaitForMail";
    }

    @Override
    protected String getFailureMessage() {
        return "no messages available at : "+url;
    }
}
