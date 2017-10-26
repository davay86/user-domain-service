package com.babcock.user.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/activateUser")
public class ActivateUserController {

    @Value("${user-service-url}")
    String userServiceUrl;

    String PENDING_URL = "/activateUser/getPending";

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(value = "/pendingUsers", method = RequestMethod.GET)
    public String getPendingUsers() {
        return restTemplate.getForEntity(getUrl(PENDING_URL), String.class).getBody();
    }

    private String getUrl(String restUrl){
        return userServiceUrl + restUrl;
    }
}
