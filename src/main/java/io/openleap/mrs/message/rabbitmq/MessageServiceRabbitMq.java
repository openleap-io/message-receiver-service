package io.openleap.mrs.message.rabbitmq;

import io.openleap.mrs.config.RabbitMQConfig;
import io.openleap.mrs.exception.ValidationException;
import io.openleap.mrs.message.MessageService;
import io.openleap.mrs.message.dto.EmailMessage;
import io.openleap.mrs.message.dto.SlackMessage;
import io.openleap.mrs.message.dto.TeamsMessage;
import io.openleap.mrs.message.validation.Schema;
import io.openleap.mrs.message.validation.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("queue.rabbitmq")
@Service
public class MessageServiceRabbitMq implements MessageService {
    public static final String VALIDATION_ERROR = "Validation error: {}";
    Logger logger = LoggerFactory.getLogger(MessageServiceRabbitMq.class);
    private final RabbitTemplate rabbitTemplate;
    private final ValidationService validationService;

    public MessageServiceRabbitMq(RabbitTemplate rabbitTemplate, ValidationService validationService) {
        this.rabbitTemplate = rabbitTemplate;
        this.validationService = validationService;
    }

    @Override
    public void sendMessage(TeamsMessage message) {
        logger.debug("Sending Teams message");
        var validationResult = validationService.validateObject(Schema.TEAMS_MESSAGE, message);
        if (!validationResult.isEmpty()) {
            logger.error("Validation errors found for Teams message");
            validationResult.forEach(vr -> logger.error(VALIDATION_ERROR, vr.getMessage()));
            throw new ValidationException();
        } else {
            rabbitTemplate.convertAndSend(RabbitMQConfig.MESSAGE_EXCHANGE, RabbitMQConfig.TEAMS_MESSAGE_ROUTE, message);
            logger.debug("Teams message sent successfully");
        }
    }

    @Override
    public void sendMessage(SlackMessage message) {
        logger.debug("Sending Slack message");
        var validationResult = validationService.validateObject(Schema.SLACK_MESSAGE, message);
        if (!validationResult.isEmpty()) {
            logger.error("Validation errors found for Slack message");
            validationResult.forEach(vr -> logger.error(VALIDATION_ERROR, vr.getMessage()));
            throw new ValidationException();
        } else {
            rabbitTemplate.convertAndSend(RabbitMQConfig.MESSAGE_EXCHANGE, RabbitMQConfig.SLACK_MESSAGE_ROUTE, message);
            logger.debug("Slack message sent successfully");
        }
    }

    @Override
    public void sendMessage(EmailMessage message) {
        logger.debug("Sending Email message");
        var validationResult = validationService.validateObject(Schema.EMAIL_MESSAGE, message);
        if (!validationResult.isEmpty()) {
            logger.error("Validation errors found for Email message");
            validationResult.forEach(vr -> logger.error(VALIDATION_ERROR, vr.getMessage()));
            throw new ValidationException();
        } else {
            rabbitTemplate.convertAndSend(RabbitMQConfig.MESSAGE_EXCHANGE, RabbitMQConfig.EMAIL_MESSAGE_ROUTE, message);
            logger.debug("Email message sent successfully");
        }
    }
}
