package com.example.be_deliveryfood.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartRequest {
    @NotNull(message = "Product ID is required")
    private Long product_id;

    @NotNull(message = "User ID is required")
    private Long user_id;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    private String add_ons;

    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getAdd_ons() {
        return add_ons;
    }

    public void setAdd_ons(String add_ons) {
        this.add_ons = add_ons;
    }
}