package io.openleap.mrs.model.message;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Custom message implementation",
        allOf = Message.class
)
public class CustomMessage extends Message {
    public CustomMessage() {
        this.messageType = MessageType.CUSTOM;
    }
}
