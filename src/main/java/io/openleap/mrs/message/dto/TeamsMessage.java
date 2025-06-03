package io.openleap.mrs.message.dto;

import io.openleap.mrs.model.message.Message;
import io.openleap.mrs.model.message.TeamsChannel;

public class TeamsMessage {
    Message message;
    String recipientId;
    TeamsChannel channel;

    public TeamsMessage(Message message, String recipientId, TeamsChannel channel) {
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

    public TeamsChannel getChannel() {
        return channel;
    }

    public void setChannel(TeamsChannel channel) {
        this.channel = channel;
    }
}
