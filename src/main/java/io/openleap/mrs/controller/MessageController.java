package io.openleap.mrs.controller;

import io.openleap.mrs.exception.ValidationException;
import io.openleap.mrs.model.message.MessageRequest;
import io.openleap.mrs.service.ReceiverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "UniApiKeycloakOauth2")
@SecurityRequirement(name = "LocalKeycloakOauth2")
@RestController
@RequestMapping("/mrs/message")
@Tag(name = "Message API", description = "Endpoints for retrieving and managing messages")
public class MessageController {
    private final ReceiverService receiverService;

    public MessageController(ReceiverService receiverService) {
        this.receiverService = receiverService;
    }

    @Operation(
            summary = "Send a message",
            description = "Accepts a message payload and queues it for asynchronous processing. " +
                    "Returns HTTP 202 Accepted if the request is valid and accepted for processing. " +
                    "Validation errors or malformed requests will result in a 400 Bad Request response."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "202",
                    description = "Message accepted for processing",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - invalid or missing authentication credentials",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request payload or validation failure",
                    content = @Content(
                            mediaType = "application/json"
                            // Optionally specify a schema for error responses
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content
            )
    })
    @PostMapping
    public ResponseEntity<Void> sendMessage(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "The message to be sent. Must include all required fields.",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = MessageRequest.class),
                    examples = {
                            @ExampleObject(
                                    name = "Teams Channel Example",
                                    summary = "Message request using the TEAMS channel and TEMPLATE message type.",
                                    value =
                                            """
                                                    {
                                                      "recipients": [
                                                        {
                                                          "id": "kirilarsov@gmail.com",
                                                          "channel": {
                                                            "channelType": "TEAMS",
                                                            "channelId": "1234567890",
                                                            "tenantId": "1234567890"
                                                          }
                                                        }
                                                      ],
                                                      "message": {
                                                        "messageType": "TEMPLATE",
                                                        "name": "Teams message template",
                                                        "templateParams": { "param1": "value1", "param2": "value2" },
                                                        "attachments": [
                                                          {
                                                            "name": "host-A.log",
                                                            "contentType": "text/plain",
                                                            "base64Data": "dGVzdA==",
                                                            "url": "https://example.com/test.txt"
                                                          }
                                                        ]
                                                      }
                                                    }
                                                    """
                            ),
                            @ExampleObject(
                                    name = "Slack Channel Example",
                                    summary = "Message request using the SLACK channel and TEMPLATE message type.",
                                    value =
                                            """
                                                    {
                                                      "recipients": [
                                                        {
                                                          "id": "kirilarsov@gmail.com",
                                                          "channel": {
                                                            "channelType": "SLACK",
                                                            "channelId": "C1234567890",
                                                            "channelName": "general"
                                                          }
                                                        }
                                                      ],
                                                      "message": {
                                                        "messageType": "TEMPLATE",
                                                        "name": "Slack message template",
                                                        "templateParams": { "param1": "value1", "param2": "value2" }
                                                      }
                                                    }
                                                    """
                            ),
                            @ExampleObject(
                                    name = "Email Channel Example",
                                    summary = "Message request using the EMAIL channel and TEMPLATE message type.",
                                    value =
                                            """
                                                    {
                                                      "recipients": [
                                                        {
                                                          "id": "kirilarsov@gmail.com",
                                                          "channel": {
                                                            "channelType": "EMAIL",
                                                            "bcc": "user@domain.com"
                                                          }
                                                        }
                                                      ],
                                                      "message": {
                                                        "messageType": "TEMPLATE",
                                                        "name": "Email message template",
                                                        "templateParams": { "param1": "value1", "param2": "value2" },
                                                        "attachments": [
                                                          {
                                                            "name": "host-A.log",
                                                            "contentType": "text/plain",
                                                            "base64Data": "dGVzdA==",
                                                            "url": "https://example.com/test.txt"
                                                          }
                                                        ]
                                                      }
                                                    }
                                                    """
                            ),
                            @ExampleObject(
                                    name = "Another Email Channel Example",
                                    summary = "Message request using the EMAIL channel and CUSTOM message type.",
                                    value =
                                            """
                                                    {
                                                      "recipients": [
                                                        {
                                                          "id": "kirilarsov@gmail.com",
                                                          "channel": {
                                                            "channelType": "EMAIL",
                                                            "bcc": "user@domain.com"
                                                          }
                                                        }
                                                      ],
                                                      "message": {
                                                        "messageType": "CUSTOM",
                                                        "name": "Email message template",
                                                        "subject": "Application Error Alert",
                                                        "body": "Take action immediately!",
                                                        "attachments": [
                                                          {
                                                            "name": "host-A.log",
                                                            "contentType": "text/plain",
                                                            "base64Data": "dGVzdA==",
                                                            "url": "https://example.com/test.txt"
                                                          }
                                                        ]
                                                      }
                                                    }
                                                    """
                            ),
                            @ExampleObject(
                                    name = "Telegram Channel Example",
                                    summary = "Message request using the TELEGRAM channel and TEMPLATE message type.",
                                    value =
                                            """
                                                    {
                                                      "recipients": [
                                                        {
                                                          "channel": {
                                                            "channelType": "TELEGRAM",
                                                            "chatId": "@oltesme"
                                                          }
                                                        }
                                                      ],
                                                      "message": {
                                                        "messageType": "TEMPLATE",
                                                        "name": "Teams message template",
                                                        "templateParams": { "param1": "value1", "param2": "value2" },
                                                        "attachments": [
                                                          {
                                                            "name": "host-A.log",
                                                            "contentType": "text/plain",
                                                            "base64Data": "dGVzdA==",
                                                            "url": "https://example.com/test.txt"
                                                          }
                                                        ]
                                                      }
                                                    }
                                                    """
                            )
                    }
            )
    ) @Valid @RequestBody MessageRequest messageRequest) {
        try {
            receiverService.receiveMessage(messageRequest);
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
