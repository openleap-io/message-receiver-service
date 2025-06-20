package io.openleap.mrs.service;

import io.openleap.mrs.config.SecurityLoggerConfig;
import io.openleap.mrs.controller.MessageController;
import io.openleap.mrs.exception.ValidationException;
import io.openleap.mrs.message.MessageService;
import io.openleap.mrs.message.logger.MessageServiceLogger;
import io.openleap.mrs.message.validation.ValidationService;
import io.openleap.mrs.model.message.Channel;
import io.openleap.mrs.model.message.MessageRequest;
import io.openleap.mrs.model.message.Recipient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

@WebMvcTest(controllers = MessageController.class)
@Import({SecurityLoggerConfig.class, MessageServiceLogger.class, ValidationService.class, ReceiverService.class})
@ActiveProfiles({"logger"})
class ReceiverServiceTest {

    @Autowired
    private ReceiverService receiverService;
    @Autowired
    private MessageService messageService;


    @Test
    void receiveMessage_shouldThrowValidationException_whenRequestIsNull() {
        assertThrows(ValidationException.class, () -> receiverService.receiveMessage(null));
    }

    @Test
    void receiveMessage_shouldThrowValidationException_whenRecipientsIsNull() {
        MessageRequest request = mock(MessageRequest.class);
        // Simulate getRecipients() returns null
        org.mockito.Mockito.when(request.getRecipients()).thenReturn(null);
        assertThrows(ValidationException.class, () -> receiverService.receiveMessage(request));
    }

    @Test
    void receiveMessage_shouldThrowValidationException_whenRecipientsIsEmpty() {
        MessageRequest request = mock(MessageRequest.class);
        org.mockito.Mockito.when(request.getRecipients()).thenReturn(Collections.emptyList());
        assertThrows(ValidationException.class, () -> receiverService.receiveMessage(request));
    }

    @Test
    void receiveMessage_shouldThrowIllegalArgumentException_whenRecipientChannelTypeIsUnsupported() {
        MessageRequest request = mock(MessageRequest.class);
        Recipient recipient = mock(Recipient.class);
        Channel channel = mock(Channel.class);

        org.mockito.Mockito.when(request.getRecipients()).thenReturn(Collections.singletonList(recipient));
        org.mockito.Mockito.when(recipient.getChannel()).thenReturn(channel);
        org.mockito.Mockito.when(channel.getChannelType()).thenReturn(null);

        assertThrows(ValidationException.class, () -> receiverService.receiveMessage(request));
    }


}