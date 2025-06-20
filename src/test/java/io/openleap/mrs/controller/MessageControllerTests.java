package io.openleap.mrs.controller;

import io.openleap.mrs.config.SecurityLoggerConfig;
import io.openleap.mrs.message.logger.MessageServiceLogger;
import io.openleap.mrs.message.validation.ValidationService;
import io.openleap.mrs.service.ReceiverService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MessageController.class)
@Import({SecurityLoggerConfig.class, MessageServiceLogger.class, ValidationService.class, ReceiverService.class})
@ActiveProfiles({"logger"})
public class MessageControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void messages_whenRecipientsMissing_shouldReturnBadRequest() throws Exception {
        mockMvc
                .perform(
                        post("/mrs/message")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void messages_whenValidEmailWithTemplate_shouldReturnAccepted() throws Exception {
        mockMvc
                .perform(
                        post("/mrs/message")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                                {
                                                  "recipients": [
                                                    {
                                                      "id": "kirilarsov@gmail.com",
                                                      "channel": {
                                                        "channelType": "EMAIL"
                                                      }
                                                    }
                                                  ],
                                                  "message": {
                                                    "messageType": "TEMPLATE",
                                                    "name": "EN_Example_Template",
                                                    "templateParams": {
                                                      "firstname": "Kiril",
                                                      "middle": "middle",
                                                      "lastname": "Arsov"
                                                    },
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
                                                """))
                .andExpect(status().isAccepted());
    }

    @Test
    void messages_whenMissingTemplateTypeInEmailWithTemplate_shouldReturnBadRequest() throws Exception {
        mockMvc
                .perform(
                        post("/mrs/message")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                                {
                                                  "recipients": [
                                                    {
                                                      "id": "kirilarsov@gmail.com",
                                                      "channel": {
                                                        "channelType": "EMAIL"
                                                      }
                                                    }
                                                  ],
                                                  "message": {
                                                    "templateParams": {
                                                      "firstname": "Kiril",
                                                      "lastname": "Arsov"
                                                    },
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
                                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void messages_whenValidEmailWithCustom_shouldReturnAccepted() throws Exception {
        mockMvc
                .perform(
                        post("/mrs/message")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                                {
                                                  "recipients": [
                                                    {
                                                      "id": "kirilarsov@gmail.com",
                                                      "channel": {
                                                        "channelType": "EMAIL"
                                                      }
                                                    }
                                                  ],
                                                  "message": {
                                                    "messageType": "CUSTOM",
                                                    "body": "Test Body",
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
                                                """))
                .andExpect(status().isAccepted());
    }

    @Test
    void messages_whenValidTeamsCustom_shouldReturnAccepted() throws Exception {
        mockMvc
                .perform(
                        post("/mrs/message")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
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
                                                    "templateParams": {
                                                      "param1": "value1",
                                                      "param2": "value2"
                                                    },
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
                                                """))
                .andExpect(status().isAccepted());
    }

    @Test
    void messages_whenTenantIdMissingTeamsTemlate_shouldReturnBadRequest() throws Exception {
        mockMvc
                .perform(
                        post("/mrs/message")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                                {
                                                  "recipients": [
                                                    {
                                                      "id": "kirilarsov@gmail.com",
                                                      "channel": {
                                                        "channelType": "TEAMS",
                                                        "channelId": "1234567890"
                                                      }
                                                    }
                                                  ],
                                                  "message": {
                                                    "messageType": "TEMPLATE",
                                                    "name": "Teams message template",
                                                    "templateParams": {
                                                      "param1": "value1",
                                                      "param2": "value2"
                                                    },
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
                                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void messages_whenValidSlackCustom_shouldReturnAccepted() throws Exception {
        mockMvc
                .perform(
                        post("/mrs/message")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
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
                                                    "name": "Teams message template",
                                                    "templateParams": {
                                                      "param1": "value1",
                                                      "param2": "value2"
                                                    },
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
                                                """))
                .andExpect(status().isAccepted());
    }

    @Test
    void messages_whenChannelIdMissingSlackCustom_shouldReturnBadRequest() throws Exception {
        mockMvc
                .perform(
                        post("/mrs/message")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                                {
                                                  "recipients": [
                                                    {
                                                      "id": "kirilarsov@gmail.com",
                                                      "channel": {
                                                      "channelType": "SLACK",
                                                      "channelName": "general"
                                                      }
                                                    }
                                                  ],
                                                  "message": {
                                                    "messageType": "TEMPLATE",
                                                    "name": "Teams message template",
                                                    "templateParams": {
                                                      "param1": "value1",
                                                      "param2": "value2"
                                                    },
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
                                                """))
                .andExpect(status().isBadRequest());
    }
}
