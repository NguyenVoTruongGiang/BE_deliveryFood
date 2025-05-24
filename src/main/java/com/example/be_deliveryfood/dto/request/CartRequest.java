package com.example.be_deliveryfood.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartRequest {
    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "User ID is required")
    private Long userId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    private String addOns;
}