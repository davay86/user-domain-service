package com.babcock.user.api.controller;

import com.babcock.user.api.message.MessageSender;
import com.babcock.user.api.payload.UserDetails;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/createUser")
public class CreateUserController {

    private static Logger logger = LoggerFactory.getLogger(CreateUserController.class);

    @Autowired
    private MessageSender messageSender;


    @HystrixCommand
    @RequestMapping(value = "/userDetails", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody List<UserDetails> userDetailsList) {

        logger.info("received userList:{}",userDetailsList);

        for (UserDetails userDetails : userDetailsList) {
            messageSender.sendCreateUserEvent(userDetails.getJsonString());
        }
    }
}
