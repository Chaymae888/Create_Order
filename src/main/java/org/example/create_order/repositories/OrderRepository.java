package org.example.create_order.repositories;

import org.example.create_order.models.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    // Recherche une commande par ID du produit
    Optional<Order> findByProductId(String productId);
}
