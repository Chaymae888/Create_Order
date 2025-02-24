package org.example.create_order.repositories;

import org.example.create_order.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository  extends MongoRepository<Product, String> {
    Page<Product> findAll(Pageable pageable);
    Page<Product> findByNameOrDescription(String name, String description, Pageable pageable);
}
