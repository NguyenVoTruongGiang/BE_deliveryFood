package com.example.be_deliveryfood.dto.repository;

import com.example.be_deliveryfood.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartIdAndProductIdAndAddOns(Long cartId, Long productId, String addOns);
}