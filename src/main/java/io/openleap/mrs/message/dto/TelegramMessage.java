package io.openleap.mrs.message.dto;


import io.openleap.mrs.model.message.Message;
import io.openleap.mrs.model.message.TelegramChannel;

public class TelegramMessage {
    Message message;
    String recipientId;
    TelegramChannel channel;

    public TelegramMessage(Message message, String recipientId, TelegramChannel channel) {
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

    public TelegramChannel getChannel() {
        return channel;
    }

    public void setChannel(TelegramChannel channel) {
        this.channel = channel;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }
}
