package com.example.be_deliveryfood.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

@Data
public class OrderRequest {
    @NotBlank(message = "Delivery address is required")
    private String deliveryAddress;

    @NotEmpty(message = "Order must contain at least one item")
    private List<OrderItemRequest> items;

    private Long cartId;

    @Data
    public static class OrderItemRequest {
        private Long productId;
        private Integer quantity;
    }
}