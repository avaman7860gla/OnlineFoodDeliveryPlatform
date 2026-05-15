package com.example.notification.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE = "notification_queue";
    public static final String EXCHANGE = "notification_exchange";
    public static final String ROUTING_KEY = "notification_routingKey";

    @Bean
    public Queue queue() {

        return QueueBuilder
                .durable(QUEUE)
                .build();
    }

    @Bean
    public TopicExchange exchange() {

        return ExchangeBuilder
                .topicExchange(EXCHANGE)
                .durable(true)
                .build();
    }

    @Bean
    public Binding binding(
            Queue queue,
            TopicExchange exchange
    ) {

        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
