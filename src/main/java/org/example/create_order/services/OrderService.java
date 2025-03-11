package org.example.create_order.services;

import org.example.create_order.exceptions.ProductNotFoundException;
import org.example.create_order.messaging.OrderEventPublisher;
import org.example.create_order.models.Order;
import org.example.create_order.models.OrderEvent;
import org.example.create_order.models.Product;
import org.example.create_order.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderEventPublisher orderEventPublisher;

    public Page<Order> allOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Optional<Order> singleOrder(String id){
        return Optional.ofNullable(orderRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Order not found with ID: " + id)));
    }

    public Order createAndProcessOrder(Product product, Order order) {
        // Calculate the price
        order.setPrice(order.getQuantity() * product.getPrice());

        // Set initial state
        order.setState(Order.OrderState.CREATED);

        // Save the order with CREATED state
        System.out.println("Step 3: Saving new order with CREATED state");
        Order savedOrder = orderRepository.save(order);

        // Create and publish order event
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setOrderId(savedOrder.getId());
        orderEvent.setProductId(savedOrder.getProductId());
        orderEvent.setQuantity(savedOrder.getQuantity());

        // Step 4: Publish the order event
        orderEventPublisher.publishOrderEvent(orderEvent);

        return savedOrder;
    }

    private void validateOrder(Product product, Order order) {
        // Just calculate the price without checking quantity
        order.setPrice(order.getQuantity() * product.getPrice());
    }
}