package com.example.be_deliveryfood.controller;

import com.example.be_deliveryfood.dto.request.CartRequest;
import com.example.be_deliveryfood.dto.request.OrderRequest;
import com.example.be_deliveryfood.entity.Cart;
import com.example.be_deliveryfood.entity.Order;
import com.example.be_deliveryfood.service.CartService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;


@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/add/{user_id}")
    public ResponseEntity<Cart> addToCart(@PathVariable("user_id") Long userId, @Valid @RequestBody CartRequest cartRequest) {
        Cart cart = cartService.addToCart(userId, cartRequest);
        if (cart == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(cart);
    }


    // Lấy giỏ hàng của user hiện tại
    @GetMapping("/my_cart")
    public ResponseEntity<Cart> getMyCart() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Cart cart = cartService.getCartByUserEmail(email);
        return ResponseEntity.ok(cart);
    }

    // Cập nhật số lượng hoặc add_ons của 1 cart item
    @PutMapping("/updateItem/{cartItemId}")
    public ResponseEntity<Cart> updateCartItem(
            @PathVariable Long cartItemId,
            @RequestBody CartRequest cartRequest
    ) {
        Cart cart = cartService.updateCartItem(cartItemId, cartRequest);
        return ResponseEntity.ok(cart);
    }

    // Xóa 1 sản phẩm khỏi giỏ hàng
    @DeleteMapping("/deleteItem/{cartItemId}")
    public ResponseEntity<Void> removeCartItem(@PathVariable Long cartItemId) {
        cartService.removeCartItem(cartItemId);
        return ResponseEntity.noContent().build();
    }

    // Xóa toàn bộ giỏ hàng
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        cartService.clearCart(email);
        return ResponseEntity.noContent().build();
    }
}