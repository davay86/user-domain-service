package com.babcock.user.api.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(MessageChannels.class)
public class MessageSender {

    private static Logger logger = LoggerFactory.getLogger(MessageSender.class);

    private MessageChannels channels;

    @Autowired
    public MessageSender(MessageChannels channels) {
        this.channels = channels;
    }

    public void sendCreateUserEvent(String message) {
        logger.info("sending message: {}",message);
        channels.createChannelOutput().send(MessageBuilder.withPayload(message).build());
    }

    public void sendActivateUserEvent(long id){
        String message = "{\"id\":\"" + id + "\"}";

        logger.info("sending message: {}",message);
        channels.activateUserChannelOutput().send(MessageBuilder.withPayload(message).build());
    }

}
