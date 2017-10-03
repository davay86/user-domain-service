package com.babcock.user.api.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MessageController.class)
@EntityScan("com.babcock.user.api.model.domain")
@ActiveProfiles("test")
public class MessageControllerTest {

    @Autowired
    private MessageController messageController;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getMessages() {

        String actual = messageController.getMessage();

        assertEquals("Demo Message", actual);
    }

}