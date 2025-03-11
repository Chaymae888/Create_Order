package org.example.create_order.messaging;

import org.example.create_order.config.RabbitMQConfig;
import org.example.create_order.models.ProductEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductEventPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Publishes a product event to the message queue
     *
     * @param productEvent The product event to publish
     */
    public void publishProductEvent(ProductEvent productEvent) {
        System.out.println("Publishing product event for product ID: " +
                productEvent.getProductId() + " with state: " + productEvent.getState() +
                " to exchange: " + RabbitMQConfig.PRODUCT_EXCHANGE);

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PRODUCT_EXCHANGE,
                RabbitMQConfig.PRODUCT_ROUTING_KEY,
                productEvent
        );
    }
}