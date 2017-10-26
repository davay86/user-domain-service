package com.babcock.user.api.controller;

import com.babcock.user.api.message.MessageSender;
import com.babcock.user.api.payload.UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/activateUser")
public class ActivateUserController {

    private static Logger logger = LoggerFactory.getLogger(ActivateUserController.class);

    @Value("${user-service-url}")
    String userServiceUrl;

    String PENDING_URL = "/activateUser/getPending";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private MessageSender messageSender;

    @RequestMapping(value = "/pendingUsers", method = RequestMethod.GET)
    public String getPendingUsers() {
        return restTemplate.getForEntity(getUrl(PENDING_URL), String.class).getBody();
    }

    @RequestMapping(value = "/activate", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void activateUser(@RequestBody List<UserDetails> userDetailsList){

        logger.info("received userList:{}",userDetailsList);

        for (UserDetails userDetails : userDetailsList) {
            messageSender.sendActivateUserEvent(userDetails.getId());
        }
    }

    private String getUrl(String restUrl){
        return userServiceUrl + restUrl;
    }
}
