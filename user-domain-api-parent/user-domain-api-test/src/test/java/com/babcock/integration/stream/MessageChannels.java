package com.babcock.integration.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

public interface MessageChannels {

    String PUBLISH_CREATE_USER_CHANNEL = "publishCreateUserChannel";

    @Output("publishCreateUserChannel")
    MessageChannel publishCreateUserChannel();

}