package io.openleap.mrs.service;

import io.openleap.mrs.exception.ValidationException;
import io.openleap.mrs.message.MessageService;
import io.openleap.mrs.model.message.MessageRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ReceiverServiceTest {

    private ReceiverService receiverService;

    @BeforeEach
    void setUp() {
        receiverService = new ReceiverService(mock(MessageService.class));
    }

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
}