package io.openleap.mrs.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class AMQPConfiguration {

    private final RabbitMQConfig rabbitIntegrationConfig;

    public AMQPConfiguration(
        RabbitMQConfig rabbitIntegrationConfig) {
        this.rabbitIntegrationConfig = rabbitIntegrationConfig;
    }

    @Bean
    public Declarables declarables() {
        List<Declarable> declarables = new ArrayList<>();
        for (var configEntry : rabbitIntegrationConfig.getExchanges()
            .entrySet()) {
            RabbitMQConfig.Exchange exchangeConfig = configEntry.getValue();
            var topicExchange = ExchangeBuilder.topicExchange(exchangeConfig.getName()).durable(true).build();
            declarables.add(topicExchange);
            for (var queueConfigEntry : exchangeConfig.getQueues().entrySet()) {
                RabbitMQConfig.Queue queueConfig = queueConfigEntry.getValue();
                var queue = QueueBuilder.durable(queueConfig.getName()).build();
                declarables.add(queue);
                for (String routingKey : queueConfig.getRoutingKeys()) {
                    var binding = new Binding(queueConfig.getName(), Binding.DestinationType.QUEUE,
                        exchangeConfig.getName(), routingKey, null);
                    declarables.add(binding);
                }
            }
        }
        return new Declarables(declarables.toArray(new Declarable[0]));
    }


    @Bean
    public MessageHandlerMethodFactory messageHandlerMethodFactory() {
        var factory = new DefaultMessageHandlerMethodFactory();

        final var jsonConverter =
            new MappingJackson2MessageConverter();
        jsonConverter.getObjectMapper().registerModule(
                new ParameterNamesModule(JsonCreator.Mode.PROPERTIES))
            .registerModule(new JavaTimeModule())
            .registerModule(new Jdk8Module());

        factory.setMessageConverter(jsonConverter);
        return factory;
    }

    @Bean
    public RabbitListenerConfigurer rabbitListenerConfigurer(
        final MessageHandlerMethodFactory messageHandlerMethodFactory) {
        return c -> c.setMessageHandlerMethodFactory(messageHandlerMethodFactory);
    }

    @Bean
    public Jackson2JsonMessageConverter converter(Jackson2ObjectMapperBuilder builder) {
        var objectMapper = builder.createXmlMapper(false).build();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        return new Jackson2JsonMessageConverter(objectMapper);
    }

}
