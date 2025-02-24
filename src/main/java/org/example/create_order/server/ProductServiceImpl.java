package org.example.create_order.server;

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

@GrpcService // Auto-configures the gRPC service
public class ProductServiceImpl extends ProductServiceGrpc.ProductServiceImplBase {

    private final ProductRepository productRepository;

    @Autowired // Inject the ProductRepository
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
                    .setQuantity(product.getQuantity()) // Ensure quantity is set
                    .build();

            responseObserver.onNext(response);
        } else {
            responseObserver.onError(new ProductNotFoundException(productId));
        }

        responseObserver.onCompleted();
    }
}