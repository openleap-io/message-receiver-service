package io.openleap.mrs.controller;

import io.openleap.mrs.model.message.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @deprecated Swagger is not adding schemas if no endpoints are defined. This is a dummy controller to ensure schemas are generated.
 */

@RestController
@Deprecated(since = "1.0.0")
@Tag(name = "Ignore", description = "Swagger is not adding schemas if no endpoints are defined. This is a dummy controller to ensure schemas are generated.")
public class DummyController {
    @GetMapping("/EmailChannel")
    public EmailChannel dummyEndpoint() {
        return new EmailChannel();
    }

    @GetMapping("/TeamsChannel")
    public TeamsChannel dummyTeamsChannelEndpoint() {
        return new TeamsChannel();
    }

    @GetMapping("/SlackChannel")
    public SlackChannel dummySlackChannelEndpoint() {
        return new SlackChannel();
    }

    @GetMapping("/CustomMessage")
    public CustomMessage dummyCustomMessageEndpoint() {
        return new CustomMessage();
    }

    @GetMapping("/TemplateMessage")
    public TemplateMessage dummyTemplateMessageEndpoint() {
        return new TemplateMessage();
    }
}
