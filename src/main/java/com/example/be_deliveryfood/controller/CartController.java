package com.example.be_deliveryfood.controller;

import com.example.be_deliveryfood.dto.request.CartRequest;
import com.example.be_deliveryfood.dto.request.OrderRequest;
import com.example.be_deliveryfood.entity.Cart;
import com.example.be_deliveryfood.entity.Order;
import com.example.be_deliveryfood.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

//    @PostMapping("/add")
//    public ResponseEntity<Cart> addToCart(@RequestParam Long userId, @Valid @RequestBody CartRequest cartRequest) {
//        return ResponseEntity.ok(cartService.addToCart(userId, cartRequest));
//    }
//
//    @PostMapping("/order")
//    public ResponseEntity<Order> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
//        return ResponseEntity.ok(cartService.createOrder(orderRequest));
//    }
}