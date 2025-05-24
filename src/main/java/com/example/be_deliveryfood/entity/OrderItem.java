package com.example.be_deliveryfood.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "order_items")
@Data
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product productItem;

    @Column(nullable = false)
    private Integer quantity;

    @Column(columnDefinition = "DECIMAL(10,2)")
    private Double price;
}