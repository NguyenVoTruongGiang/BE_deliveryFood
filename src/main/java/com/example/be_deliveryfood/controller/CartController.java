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
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(@RequestParam Long userId, @Valid @RequestBody CartRequest cartRequest) {
        Cart cart = cartService.addToCart(userId, cartRequest);
        if (cart == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(cart);
    }

//    @PostMapping("/order")
//    public ResponseEntity<Order> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
//        return ResponseEntity.ok(cartService.createOrder(orderRequest));
//    }
}