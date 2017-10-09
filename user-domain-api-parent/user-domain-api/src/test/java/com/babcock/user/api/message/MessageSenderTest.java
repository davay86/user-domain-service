package com.babcock.user.api.message;

import com.babcock.user.api.application.TestApplication;
import com.babcock.user.api.configuration.ConfigurationForTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.annotation.Bindings;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MessageSender.class)
@EnableBinding(MessageChannels.class)
public class MessageSenderTest {

    @Autowired
    private MessageChannels messageChannels;

    @Autowired
    private MessageCollector messageCollector;

    @Autowired
    private MessageSender messageSender;

    @Test
    public void sendCreateUserEvent() throws Exception {

        messageSender.sendCreateUserEvent("hello channel 1");

        Message<String> received = getMessageFromChannel(messageChannels.createChannelOutput());
        assertThat(received.getPayload(), equalTo("Channel One Message (hello channel 1)"));
    }

    private Message<String> getMessageFromChannel(MessageChannel channel) {
        return (Message<String>) messageCollector.forChannel(channel).poll();
    }

}