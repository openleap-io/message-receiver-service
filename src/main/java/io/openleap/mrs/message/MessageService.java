package io.openleap.mrs.message;

import io.openleap.mrs.exception.ValidationException;
import io.openleap.mrs.message.dto.EmailMessage;
import io.openleap.mrs.message.dto.SlackMessage;
import io.openleap.mrs.message.dto.TeamsMessage;

public interface MessageService {
    void sendMessage(TeamsMessage message) throws ValidationException;

    void sendMessage(SlackMessage message) throws ValidationException;

    void sendMessage(EmailMessage message) throws ValidationException;
}
