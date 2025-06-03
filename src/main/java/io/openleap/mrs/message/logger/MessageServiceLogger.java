package io.openleap.mrs.message.logger;

import io.openleap.mrs.exception.ValidationException;
import io.openleap.mrs.message.MessageService;
import io.openleap.mrs.message.dto.EmailMessage;
import io.openleap.mrs.message.dto.SlackMessage;
import io.openleap.mrs.message.dto.TeamsMessage;
import io.openleap.mrs.message.validation.Schema;
import io.openleap.mrs.message.validation.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("logger")
@Service
public class MessageServiceLogger implements MessageService {
    public static final String VALIDATION_ERROR = "Validation error: {}";
    Logger logger = LoggerFactory.getLogger(MessageServiceLogger.class);

    private final ValidationService validationService;

    public MessageServiceLogger(ValidationService validationService) {
        this.validationService = validationService;
    }

    @Override
    public void sendMessage(TeamsMessage message) {
        logger.info("Sending Teams message");
        var validationResult = validationService.validateObject(Schema.TEAMS_MESSAGE, message);
        if (!validationResult.isEmpty()) {
            logger.error("Validation errors found for Teams message");
            validationResult.forEach(vr -> logger.error(VALIDATION_ERROR, vr.getMessage()));

            throw new ValidationException();
        }
    }

    @Override
    public void sendMessage(SlackMessage message) {
        logger.info("Sending Slack message");
        var validationResult = validationService.validateObject(Schema.SLACK_MESSAGE, message);
        if (!validationResult.isEmpty()) {
            logger.error("Validation errors found for Slack message");
            validationResult.forEach(vr -> logger.error(VALIDATION_ERROR, vr.getMessage()));
            throw new ValidationException();
        }
    }

    @Override
    public void sendMessage(EmailMessage message) {
        logger.info("Sending Email message");
        var validationResult = validationService.validateObject(Schema.EMAIL_MESSAGE, message);
        if (!validationResult.isEmpty()) {
            logger.error("Validation errors found for Email message");
            validationResult.forEach(vr -> logger.error(VALIDATION_ERROR, vr.getMessage()));
            throw new ValidationException();
        }
    }
}
