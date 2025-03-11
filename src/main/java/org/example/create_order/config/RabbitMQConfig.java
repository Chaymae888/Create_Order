package org.example.create_order.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Queue names
    public static final String ORDER_QUEUE = "order_queue";
    public static final String PRODUCT_QUEUE = "product_queue";

    // Exchange names
    public static final String ORDER_EXCHANGE = "order_exchange";
    public static final String PRODUCT_EXCHANGE = "product_exchange";

    // Routing keys
    public static final String ORDER_ROUTING_KEY = "order.event";
    public static final String PRODUCT_ROUTING_KEY = "product.event";

    // Order Queue Configuration
    @Bean
    public Queue orderQueue() {
        return new Queue(ORDER_QUEUE, true);
    }

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Binding orderBinding(Queue orderQueue, TopicExchange orderExchange) {
        return BindingBuilder.bind(orderQueue).to(orderExchange).with(ORDER_ROUTING_KEY);
    }

    // Product Queue Configuration
    @Bean
    public Queue productQueue() {
        return new Queue(PRODUCT_QUEUE, true);
    }

    @Bean
    public TopicExchange productExchange() {
        return new TopicExchange(PRODUCT_EXCHANGE);
    }

    @Bean
    public Binding productBinding(Queue productQueue, TopicExchange productExchange) {
        return BindingBuilder.bind(productQueue).to(productExchange).with(PRODUCT_ROUTING_KEY);
    }

    // Message converter
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // RabbitTemplate configuration
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}