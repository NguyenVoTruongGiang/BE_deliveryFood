package com.example.be_deliveryfood.service;

import com.example.be_deliveryfood.entity.*;
import com.example.be_deliveryfood.dto.request.CartRequest;
import com.example.be_deliveryfood.dto.repository.*;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Cart addToCart(Long userId, CartRequest cartRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ValidationException("User not found"));
        
        Product product = productRepository.findById(cartRequest.getProduct_id())
                .orElseThrow(() -> new ValidationException("Product not found"));
        
        String addOns = cartRequest.getAdd_ons() != null ? cartRequest.getAdd_ons() : "";
        
        // Find or create cart for this user
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
        
        // Find cart item if it exists (you'll need to create this repository method)
        CartItem cartItem = cartItemRepository.findByCartIdAndProductIdAndAddOns(
                cart.getId(), product.getId(), addOns).orElse(null);
        
        if (cartItem == null) {
            // Create new cart item
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setAddOns(addOns);
            cartItem.setQuantity(cartRequest.getQuantity() != null ? cartRequest.getQuantity() : 1);
            cart.addCartItem(cartItem);
        } else {
            // Update existing cart item
            Integer newQuantity = cartRequest.getQuantity() != null ? 
                    cartRequest.getQuantity() : cartItem.getQuantity() + 1;
            
            if (newQuantity <= 0) {
                // Remove cart item if quantity is zero or negative
                cart.removeCartItem(cartItem);
                cartItemRepository.delete(cartItem);
            } else {
                cartItem.setQuantity(newQuantity);
            }
        }
        
        return cartRepository.save(cart);
    }
}