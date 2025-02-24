package org.example.create_order.services;

import org.example.create_order.exceptions.ProductNotFoundException;
import org.example.create_order.models.Order;
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

    public Page<Order> allOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }
    public Optional<Order> singleOrder(String id){
        return Optional.ofNullable(orderRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Order not found with ID: " + id)));
    }

    public Order createOrder(Product product,Order order) {
        validateOrder(product,order);
        return orderRepository.save(order);
    }

    private void validateOrder(Product product, Order order) {

        // Check if order quantity exceeds product quantity
        if (order.getQuantity() > product.getQuantity()) {
            throw new RuntimeException("The quantity surpasses the product's available quantity");
        } else {
            order.setPrice(order.getQuantity() * product.getPrice());
        }
    }


}
