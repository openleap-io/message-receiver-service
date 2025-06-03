package io.openleap.mrs.model.message;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Message type for API request")
public enum MessageType {
    TEMPLATE,
    CUSTOM

}
