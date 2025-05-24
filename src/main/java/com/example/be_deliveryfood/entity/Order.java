package com.example.be_deliveryfood.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 255)
    private String deliveryAddress;

    @Column(columnDefinition = "DECIMAL(10,2)")
    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private OrderStatus status;

    @Column
    private LocalDateTime createdAt;

    public enum OrderStatus {
        PENDING, PREPARING, DELIVERING, COMPLETED, CANCELLED
    }


}
