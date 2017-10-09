package com.babcock.user.api.message;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface MessageChannels {

    String CREATE_CHANNEL_OUTPUT = "createChannelOutput";

    @Output(CREATE_CHANNEL_OUTPUT)
    MessageChannel createChannelOutput();
}
