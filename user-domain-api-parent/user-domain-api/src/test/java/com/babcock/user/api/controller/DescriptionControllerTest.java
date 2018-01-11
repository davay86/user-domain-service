package com.babcock.user.api.controller;

import com.babcock.user.api.application.TestApplication;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class, webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class DescriptionControllerTest {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    DescriptionController descriptionController;


    @Before
    public void setup(){

    }

    @Test
    public void returnDescriptionMessage() {
        String returnString = "This is the user-domain-api microservice";

        String description = descriptionController.getDescriptionMessage();

        Assert.assertEquals(returnString,description);
    }
}
