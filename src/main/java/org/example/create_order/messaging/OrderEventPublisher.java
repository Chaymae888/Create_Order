package org.example.create_order.messaging;

import org.example.create_order.config.RabbitMQConfig;
import org.example.create_order.models.OrderEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Publishes an order event to the message queue
     *
     * @param orderEvent The order event to publish
     */
    public void publishOrderEvent(OrderEvent orderEvent) {
        System.out.println("Publishing order event for order ID: " +
                orderEvent.getOrderId() + " to exchange: " + RabbitMQConfig.ORDER_EXCHANGE);

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_EXCHANGE,
                RabbitMQConfig.ORDER_ROUTING_KEY,
                orderEvent
        );
    }
}