package io.openleap.mrs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties("message-receiver-service.rabbitmq")
public class RabbitMQConfig {
    public static final String MESSAGE_EXCHANGE = "messages-exchange";
    public static final String TEAMS_MESSAGE_ROUTE = "messages.channel.teams.add";
    public static final String SLACK_MESSAGE_ROUTE = "messages.channel.slack.add";
    public static final String EMAIL_MESSAGE_ROUTE = "messages.channel.email.add";
    public static final String TELEGRAM_MESSAGE_ROUTE = "messages.channel.telegram.add";
    private Map<String, Exchange> exchanges;

    public Map<String, Exchange> getExchanges() {
        return exchanges;
    }

    public void setExchanges(
            Map<String, Exchange> exchanges) {
        this.exchanges = exchanges;
    }


    public static class Exchange {
        private String name;
        private Map<String, Queue> queues;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Map<String, Queue> getQueues() {
            return queues;
        }

        public void setQueues(
                Map<String, Queue> queues) {
            this.queues = queues;
        }
    }

    public static class Queue {
        private String name;
        private List<String> routingKeys;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getRoutingKeys() {
            return routingKeys;
        }

        public void setRoutingKeys(List<String> routingKeys) {
            this.routingKeys = routingKeys;
        }
    }

}
