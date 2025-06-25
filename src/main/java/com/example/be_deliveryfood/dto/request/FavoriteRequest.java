package com.example.be_deliveryfood.dto.request;

import com.example.be_deliveryfood.entity.Product;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class FavoriteRequest {
    @NotNull(message = "Product ID is required")
    private Long product_id;

    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }
}