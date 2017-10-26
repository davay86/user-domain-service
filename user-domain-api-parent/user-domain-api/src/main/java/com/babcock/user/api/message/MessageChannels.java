package com.babcock.user.api.message;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface MessageChannels {

    String CREATE_CHANNEL_OUTPUT = "createChannelOutput";
    String ACTIVATE_USER_CHANNEL_OUTPUT = "activateUserChannelOutput";

    @Output(CREATE_CHANNEL_OUTPUT)
    MessageChannel createChannelOutput();

    @Output(ACTIVATE_USER_CHANNEL_OUTPUT)
    MessageChannel activateUserChannelOutput();
}
