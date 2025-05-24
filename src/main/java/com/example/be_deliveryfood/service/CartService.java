package com.example.be_deliveryfood.service;

import com.example.be_deliveryfood.entity.*;
import com.example.be_deliveryfood.dto.request.CartRequest;
import com.example.be_deliveryfood.dto.request.OrderRequest;
import com.example.be_deliveryfood.dto.repository.*;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;
//
//    @Transactional
//    public Cart addToCart(Long userId, CartRequest cartRequest) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new ValidationException("User not found"));
//        Product product = productRepository.findById(cartRequest.getProductId())
//                .orElseThrow(() -> new ValidationException("Product not found"));
//
//        // Check if a cart exists for this user and product
//        Cart cart = cartRepository.findByUserIdAndProductId(userId, cartRequest.getProductId())
//                .orElseGet(() -> {
//                    Cart newCart = new Cart();
//                    newCart.setUser(user);
//                    newCart.setProduct(product);
//                    newCart.setAddOns(cartRequest.getAddOns() != null ? cartRequest.getAddOns() : "");
//                    return cartRepository.save(newCart);
//                });
//
//        // Update or create OrderItem for quantity
//        OrderItem orderItem = orderItemRepository.findByCartId(cart.getId())
//                .orElseGet(() -> {
//                    OrderItem newItem = new OrderItem();
//                    newItem.setCart(cart);
//                    newItem.setProduct(product);
//                    newItem.setPrice(product.getPrice());
//                    return newItem;
//                });
//
//        orderItem.setQuantity(cartRequest.getQuantity());
//        orderItemRepository.save(orderItem);
//
//        return cart;
//    }

//    @Transactional
//    public Order createOrder(OrderRequest orderRequest) {
//        Cart cart = cartRepository.findById(orderRequest.getCartId())
//                .orElseThrow(() -> new ValidationException("Cart not found"));
//
//        OrderItem cartItem = orderItemRepository.findByCartId(cart.getId())
//                .orElseThrow(() -> new ValidationException("No items in cart"));
//
//        Order order = new Order();
//        order.setUser(cart.getUser());
//        order.setCreatedAt(LocalDateTime.now());
//        order.setStatus(Order.OrderStatus.PENDING);
//        order.setDeliveryAddress(orderRequest.getDeliveryAddress() != null ? orderRequest.getDeliveryAddress() : cart.getUser().getAddress());
//        order.setTotalPrice(cartItem.getPrice() * cartItem.getQuantity());
//
//        OrderItem orderItem = new OrderItem();
//        orderItem.setOrder(order);
//        orderItem.setProduct(cartItem.getProduct());
//        orderItem.setQuantity(cartItem.getQuantity());
//        orderItem.setPrice(cartItem.getPrice());
//        orderItemRepository.save(orderItem);
//
//        // Clear cart
//        orderItemRepository.delete(cartItem);
//        cartRepository.delete(cart);
//
//        return orderRepository.save(order);
//    }
}