package org.example.create_order.messaging;

import org.example.create_order.models.OrderEvent;
import org.example.create_order.models.ProductEvent;
import org.example.create_order.models.Order;
import org.example.create_order.repositories.OrderRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProductEventConsumer {

    @Autowired
    private OrderRepository orderRepository;

    /**
     * Listens for product events and updates the order status accordingly
     *
     * @param productEvent The product event received from the queue
     */
    @RabbitListener(queues = "${spring.rabbitmq.product-queue}")
    public void handleProductEvent(ProductEvent productEvent) {
        System.out.println("Product event consumer received event for order ID: " +
                productEvent.getOrderId() + " with state: " + productEvent.getState());

        String orderId = productEvent.getOrderId();
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();

            // Update order status based on product availability
            if (productEvent.getState() == ProductEvent.ProductState.AVAILABLE) {
                System.out.println("Service: Updating order ID: " + orderId + " to PROCESSING state");
                order.setState(Order.OrderState.PROCESSING);
            } else if (productEvent.getState() == ProductEvent.ProductState.OUT_OF_STOCK) {
                System.out.println("Service: Updating order ID: " + orderId + " to FAILED state");
                order.setState(Order.OrderState.FAILED);
            }

            orderRepository.save(order);
            System.out.println("Service: Order updated successfully");
        }
    }
}