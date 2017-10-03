package com.babcock.user.api.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@RestController
@CrossOrigin
@RequestMapping("/message")
public class MessageController {

    private Logger logger = Logger.getLogger(getClass().getName());

    @RequestMapping(value = "/message", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getMessage() {

        logger.info("getMessage() invoked");

        String messages = "Demo Message";

        logger.info("getMessage() found: " + messages);

        return messages;
    }
}
