package com.example.be_deliveryfood.dto.request;

import lombok.Data;

@Data
public class OrderItemRequest {

    private Long product_id;
    private Integer quantity;

    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
