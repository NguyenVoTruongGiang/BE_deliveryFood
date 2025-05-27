package com.example.be_deliveryfood.dto.repository;

import com.example.be_deliveryfood.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(Long userId);
    
    // Replace the old existsByProductId method with a JPQL query that checks cart items
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Cart c JOIN c.cartItems ci WHERE ci.product.id = :productId")
    Boolean existsByCartItemsProductId(@Param("productId") Long productId);
    
    // Similarly, update the findByProductId method
    @Query("SELECT c FROM Cart c JOIN c.cartItems ci WHERE ci.product.id = :productId")
    Optional<Cart> findByCartItemsProductId(@Param("productId") Long productId);
    
    // And the findByUserIdAndProductIdAndAddOns method
    @Query("SELECT c FROM Cart c JOIN c.cartItems ci WHERE c.user.id = :userId AND ci.product.id = :productId AND ci.addOns = :addOns")
    Optional<Cart> findByUserIdAndCartItemsProductIdAndAddOns(
            @Param("userId") Long userId, 
            @Param("productId") Long productId, 
            @Param("addOns") String addOns);
}