package com.example.be_deliveryfood.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "carts")
@Data
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String addOns;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getAddOns() {
        return addOns;
    }

    public void setAddOns(String addOns) {
        this.addOns = addOns;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}