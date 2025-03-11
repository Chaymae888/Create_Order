package org.example.create_order.models;

public class OrderEvent {
    private String orderId;
    private String productId;
    private int quantity;
    private OrderState state;

    // No-argument constructor
    public OrderEvent() {
    }

    // All-arguments constructor
    public OrderEvent(String orderId, String productId, int quantity, OrderState state) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.state = state;
    }

    // Getters and Setters (you can either manually create them or use an IDE to generate them)
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    // Enum for OrderState
    public enum OrderState {
        CREATED,
        PROCESSING,
        FAILED
    }
}
