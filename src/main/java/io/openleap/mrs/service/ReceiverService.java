package io.openleap.mrs.service;

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
        request.getRecipients().forEach(recipient -> {
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
                    throw new IllegalArgumentException("Unsupported recipient type: " + recipient.getChannel().getChannelType());
            }
        });
    }
}
