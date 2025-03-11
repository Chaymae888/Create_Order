package org.example.create_order.messaging;

import org.example.create_order.models.OrderEvent;
import org.example.create_order.models.Product;
import org.example.create_order.models.ProductEvent;
import org.example.create_order.repositories.ProductRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderEventConsumer {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductEventPublisher productEventPublisher;

    /**
     * Listens for order events and processes product availability
     *
     * @param orderEvent The order event received from the queue
     */
    @RabbitListener(queues = "${spring.rabbitmq.order-queue}")
    public void handleOrderEvent(OrderEvent orderEvent) {
        System.out.println("Step 5: Order event consumer received event for order ID: " +
                orderEvent.getOrderId() + " and product ID: " + orderEvent.getProductId());
        String productId = orderEvent.getProductId();
        Optional<Product> productOptional = productRepository.findById(productId);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            int requestedQuantity = orderEvent.getQuantity();

            ProductEvent productEvent = new ProductEvent();
            productEvent.setProductId(productId);
            productEvent.setOrderId(orderEvent.getOrderId());

            // Check if product is available
            if (product.getQuantity() >= requestedQuantity) {
                System.out.println("the product is available ,Updating product quantity in database");
                // Update product quantity
                product.setQuantity(product.getQuantity() - requestedQuantity);
                productRepository.save(product);

                // Send AVAILABLE product event
                productEvent.setState(ProductEvent.ProductState.AVAILABLE);
            } else {
                // Send OUT_OF_STOCK product event
                System.out.println("the product is out of stock");
                productEvent.setState(ProductEvent.ProductState.OUT_OF_STOCK);
            }

            // Publish product event
            productEventPublisher.publishProductEvent(productEvent);
        }
    }
}