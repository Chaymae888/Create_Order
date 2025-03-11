package org.example.create_order.models;

import com.fasterxml.jackson.annotation.JsonTypeId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    private String id;
    private Double price;
    private String productId;
    private int quantity;
    private OrderState state; // Added field to track order state

    public enum OrderState {
        CREATED,
        PROCESSING,
        FAILED
    }

    public String getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
    }
}