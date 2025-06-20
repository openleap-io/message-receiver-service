package io.openleap.mrs.service;

import io.openleap.mrs.exception.ValidationException;
import io.openleap.mrs.message.MessageService;
import io.openleap.mrs.message.dto.EmailMessage;
import io.openleap.mrs.message.dto.SlackMessage;
import io.openleap.mrs.message.dto.TeamsMessage;
import io.openleap.mrs.message.dto.TelegramMessage;
import io.openleap.mrs.model.message.*;
import org.springframework.stereotype.Service;

@Service
public class ReceiverService {
    private final MessageService messageService;

    public ReceiverService(MessageService messageService) {
        this.messageService = messageService;
    }

    public void receiveMessage(MessageRequest request) {
        if (request == null || request.getRecipients() == null || request.getRecipients().isEmpty()) {
            throw new ValidationException();
        }

        request.getRecipients().forEach(recipient -> {
            if (recipient.getChannel() == null || recipient.getChannel().getChannelType() == null) {
                throw new ValidationException("Recipient channel type is not supported or is null");
            }
            switch (recipient.getChannel().getChannelType()) {
                case TEAMS:
                    messageService.sendMessage(new TeamsMessage(
                            request.getMessage(),
                            recipient.getId(),
                            (TeamsChannel) recipient.getChannel()
                    ));
                    break;
                case SLACK:
                    messageService.sendMessage(new SlackMessage(
                            request.getMessage(),
                            recipient.getId(),
                            (SlackChannel) recipient.getChannel()
                    ));
                    break;
                case EMAIL:
                    messageService.sendMessage(new EmailMessage(
                            request.getMessage(),
                            recipient.getId(),
                            (EmailChannel) recipient.getChannel()
                    ));
                    break;
                case TELEGRAM:
                    messageService.sendMessage(new TelegramMessage(
                            request.getMessage(),
                            recipient.getId(),
                            (TelegramChannel) recipient.getChannel()
                    ));
                    break;
                default:
                    throw new ValidationException("Unsupported recipient type: " + recipient.getChannel().getChannelType());
            }
        });

    }
}
