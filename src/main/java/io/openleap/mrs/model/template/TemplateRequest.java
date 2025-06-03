package io.openleap.mrs.model.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Template request model")
public class TemplateRequest {
    @Schema(description = "Template name", example = "test-template")
    @JsonProperty("name")
    String name;

    @Schema(description = "Subject of the template", example = "Welcome to OpenLeap")
    @JsonProperty("subject")
    String subject;

    @Schema(description = "Body of the template", example = "Hello {user}, welcome to OpenLeap!")
    @JsonProperty("body")
    String body;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
