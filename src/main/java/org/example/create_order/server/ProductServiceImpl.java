package org.example.create_order.server;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.create_order.exceptions.ProductNotFoundException;
import org.example.create_order.models.Product;
import org.example.create_order.models.ProductRequest;
import org.example.create_order.models.ProductResponse;
import org.example.create_order.models.ProductServiceGrpc;
import org.example.create_order.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@GrpcService
public class ProductServiceImpl extends ProductServiceGrpc.ProductServiceImplBase {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void getProductById(ProductRequest request, StreamObserver<ProductResponse> responseObserver) {
        String productId = request.getProductId();
        Optional<Product> productOptional = productRepository.findById(productId);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            ProductResponse response = ProductResponse.newBuilder()
                    .setProductId(product.getId())
                    .setName(product.getName())
                    .setDescription(product.getDescription())
                    .setPrice(product.getPrice())
                    .setQuantity(product.getQuantity())
                    .build();

            responseObserver.onNext(response);
        } else {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("Product not found with ID: " + productId)
                    .asRuntimeException());}

        responseObserver.onCompleted();
    }
}