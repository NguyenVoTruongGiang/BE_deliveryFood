package com.example.be_deliveryfood.dto.repository;

import com.example.be_deliveryfood.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    // tìm kiếm theo userId
    Optional<Cart> findByUserId(Long userId);

    // tìm kiếm theo userId và productId và addOns
    Optional<Cart> findByUserIdAndProductIdAndAddOns(Long userId, Long productId, String addOns);

    // tìm kiếm theo productId
    Optional<Cart> findByProductId(Long productId);

    // kiểm tra product trong cart có tồn tại hay chưa
    Boolean existsByProductId(Long productId);
}