package org.example.create_order.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.create_order.models.ProductRequest;
import org.example.create_order.models.ProductResponse;
import org.example.create_order.models.ProductServiceGrpc;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
//
@Component
public class OrderClient {

    private ManagedChannel channel;
    private ProductServiceGrpc.ProductServiceBlockingStub productServiceBlockingStub;

    @PostConstruct
    private void init() {
        // Initialize the communication channel with the gRPC server address and port
        channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext() // For simplicity, use plaintext (no TLS)
                .build();

        // Initialize the blocking/synchronous stub for ProductService
        productServiceBlockingStub = ProductServiceGrpc.newBlockingStub(channel);
    }

    /**
     * Fetches product details by ID using the ProductService gRPC server.
     *
     * @param productId The ID of the product to fetch.
     * @return ProductResponse containing product details.
     */
    public ProductResponse getProductById(String productId) {
        System.out.println("gRPC client requesting product details for ID: " + productId);
        ProductRequest request = ProductRequest.newBuilder()
                .setProductId(productId)
                .build();

        ProductResponse response = productServiceBlockingStub.getProductById(request);
        System.out.println("gRPC: Received product details from ProductService for product ID: " + productId);
        return response;
    }



}