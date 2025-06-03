package io.openleap.mrs.model.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Channel model for API request.",
        discriminatorProperty = "channelType",
        subTypes = {EmailChannel.class, TeamsChannel.class, SlackChannel.class,}
)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "channelType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = EmailChannel.class, name = "EMAIL"),
        @JsonSubTypes.Type(value = TeamsChannel.class, name = "TEAMS"),
        @JsonSubTypes.Type(value = SlackChannel.class, name = "SLACK")
})
public abstract class Channel {
    @Schema(
            description = "Channel type",
            example = "TEAMS"
    )
    @JsonProperty(value = "channelType", required = true)
    ChannelType channelType;

    public ChannelType getChannelType() {
        return channelType;
    }

    public void setChannelType(ChannelType channelType) {
        this.channelType = channelType;
    }

    Channel() {
    }
}
