package com.babcock.user.api.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/description")
public class DescriptionController {

    private static Logger logger = LoggerFactory.getLogger(DescriptionController.class);

    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand
    @RequestMapping(value = "/message", method = RequestMethod.GET)
    public String getDescriptionMessage() {

        logger.info("Received description message request.");

        String description = "This is the user-domain-api microservice";

        return description;
    }
}
