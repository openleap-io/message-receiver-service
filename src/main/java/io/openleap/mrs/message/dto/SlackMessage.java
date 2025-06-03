package io.openleap.mrs.message.dto;

import io.openleap.mrs.model.message.Message;
import io.openleap.mrs.model.message.SlackChannel;

public class SlackMessage {
    Message message;
    String recipientId;
    SlackChannel channel;


    public SlackMessage(Message message, String recipientId, SlackChannel channel) {
        this.message = message;
        this.recipientId = recipientId;
        this.channel = channel;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public SlackChannel getChannel() {
        return channel;
    }

    public void setChannel(SlackChannel channel) {
        this.channel = channel;
    }
}
