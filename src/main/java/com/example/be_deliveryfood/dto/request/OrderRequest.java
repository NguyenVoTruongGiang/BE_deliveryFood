package com.example.be_deliveryfood.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

@Data
public class OrderRequest {
    @NotBlank(message = "Delivery address is required")
    private String delivery_address;

    @NotEmpty(message = "Order must contain at least one item")
    private List<OrderItemRequest> items;

    private Long cart_id;

    @Data
    public static class OrderItemRequest {
        private Long product_id;
        private Integer quantity;
    }
}