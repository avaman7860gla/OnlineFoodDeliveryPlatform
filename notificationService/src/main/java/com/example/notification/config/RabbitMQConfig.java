package com.example.notification.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
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
        return new Queue(QUEUE);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter converter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper typeMapper = 
            new org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper();
        
        typeMapper.setTrustedPackages("*");
        
        java.util.Map<String, Class<?>> idClassMapping = new java.util.HashMap<>();
        idClassMapping.put("com.example.order.dto.Notification", com.example.notification.entity.Notification.class);
        typeMapper.setIdClassMapping(idClassMapping);
        
        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }
}
