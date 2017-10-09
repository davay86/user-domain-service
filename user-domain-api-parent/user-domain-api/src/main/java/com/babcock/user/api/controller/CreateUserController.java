package com.babcock.user.api.controller;

import com.babcock.user.api.message.MessageSender;
import com.babcock.user.api.payload.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/createUser")
public class CreateUserController {

    @Autowired
    private MessageSender messageSender;

    private Logger logger = Logger.getLogger(getClass().getName());

    @RequestMapping(value = "/userDetails", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody List<UserDetails> userDetailsList) {

        for (UserDetails userDetails : userDetailsList) {
            messageSender.sendCreateUserEvent(userDetails.getJsonString());
        }
    }
}
