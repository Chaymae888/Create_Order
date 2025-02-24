package org.example.create_order.services;


import org.example.create_order.exceptions.ProductNotFoundException;
import org.example.create_order.models.Product;
import org.example.create_order.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    public Page<Product> allProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Optional<Product> singleProduct(String id){
        return Optional.ofNullable(productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id)));
    }
    public Page<Product> searchProducts(String query, Pageable pageable) {
        return productRepository.findByNameOrDescription(query, query, pageable);
    }

    public Product createProduct(Product Product) {

        validateProductFields(Product);


        return productRepository.save(Product);
    }

    private void validateProductFields(Product product) {
        if (product.getName() == null || product.getName().isEmpty()) {
            throw new RuntimeException("Product name is required");
        }
        if (product.getPrice() == null || product.getPrice().isNaN() || product.getPrice()==0) {
            throw new RuntimeException("Price is required and should not be 0");
        }
    }

    public Product updateProduct(String id, Product updatedProduct) {
        return productRepository.findById(id)
                .map(Product -> {
                    Product.setName(updatedProduct.getName());
                    Product.setPrice(updatedProduct.getPrice());
                    Product.setDescription(updatedProduct.getDescription());
                    Product.setQuantity(updatedProduct.getQuantity());
                    return productRepository.save(Product);
                }).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }


}
