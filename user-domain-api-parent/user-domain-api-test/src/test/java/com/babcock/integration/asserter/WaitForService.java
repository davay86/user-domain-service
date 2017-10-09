package com.babcock.integration.asserter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class WaitForService extends WaitUntilAsserter {

    private String url;
    private RestTemplate restTemplate;

    public WaitForService(String url, RestTemplate restTemplate) {
        super();
        this.url = url;
        this.restTemplate = restTemplate;
    }

    @Override
    protected boolean execute() {
        try {

            System.out.println("calling url :"+url);

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            System.out.print("Status Code ("+response.getStatusCodeValue()+") : "+response.getStatusCode().is2xxSuccessful());

            if(response.getStatusCode().is2xxSuccessful()) {
                return true;
            }else {
                System.out.print(".");
                return false;
            }

        }catch (Exception ex) {
            //System.err.println(ex);
            System.out.print(".");
            return false;
        }
    }

    @Override
    protected String getTaskName() {
        return "WaitForService";
    }

    @Override
    protected String getFailureMessage() {
        return url+" not available";
    }
}
