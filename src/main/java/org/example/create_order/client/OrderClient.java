package org.example.create_order.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.example.create_order.exceptions.ProductNotFoundException;
import org.example.create_order.models.ProductRequest;
import org.example.create_order.models.ProductResponse;
import org.example.create_order.models.ProductServiceGrpc;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class OrderClient {

    private ManagedChannel channel;
    private ProductServiceGrpc.ProductServiceBlockingStub productServiceBlockingStub;

    @PostConstruct
    private void init() {
        channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext() // For simplicity, use plaintext (no TLS)
                .build();

        productServiceBlockingStub = ProductServiceGrpc.newBlockingStub(channel);
    }
    public ProductResponse getProductById(String productId) {
        System.out.println("gRPC client requesting product details for ID: " + productId);
        ProductRequest request = ProductRequest.newBuilder()
                .setProductId(productId)
                .build();
        try {
            System.out.println("gRPC: Received product details from ProductService for product ID: " + productId);
            return productServiceBlockingStub.getProductById(request);
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.NOT_FOUND) {
                throw new ProductNotFoundException("Product not found with ID: " + productId);
            } else {
                throw new RuntimeException("Failed to retrieve product: " + e.getMessage(), e);
            }
        }
    }



}