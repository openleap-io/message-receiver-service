package io.openleap.mrs.model.message;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Type of notification channel")
public enum ChannelType {
    EMAIL,
    SLACK,
    TEAMS;

}
