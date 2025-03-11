package org.example.create_order.models;

public class ProductEvent {
    private String productId;
    private String orderId;
    private ProductState state;

    // No-argument constructor
    public ProductEvent() {
    }

    // All-argument constructor
    public ProductEvent(String productId, String orderId, ProductState state) {
        this.productId = productId;
        this.orderId = orderId;
        this.state = state;
    }

    // Getters and Setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public ProductState getState() {
        return state;
    }

    public void setState(ProductState state) {
        this.state = state;
    }

    // Enum for ProductState
    public enum ProductState {
        AVAILABLE,
        OUT_OF_STOCK
    }
}
