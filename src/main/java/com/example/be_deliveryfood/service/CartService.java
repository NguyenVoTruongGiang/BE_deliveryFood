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

    @Transactional
    public Cart addToCart(Long user_id, CartRequest cartRequest) {
        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new ValidationException("User not found"));
        Product product = productRepository.findById(cartRequest.getProduct_id())
                .orElseThrow(() -> new ValidationException("Product not found"));

        String addOns = cartRequest.getAdd_ons() != null ? cartRequest.getAdd_ons() : "";

        // Tìm cart theo user, product, addOns
        Cart cart = cartRepository.findByUserIdAndProductIdAndAddOns(user_id, cartRequest.getProduct_id(), addOns)
                .orElse(null);
        if (user_id != null || cartRequest.getProduct_id() != null || addOns != null) {
            // tạo cart mới nếu không có cart tồn tại
            cart = new Cart();
            cart.setUser(user);
            cart.setProduct(product);
            cart.setAddOns(cartRequest.getAdd_ons() != null ? cartRequest.getAdd_ons() : "");
            cart = cartRepository.save(cart);
        }

        // Kiểm tra xem có OrderItem nào hiện có trong giỏ hàng không?
        OrderItem orderItem = orderItemRepository.findByCartId(cart.getId())
                .orElse(null);
        if (orderItem == null) {
            orderItem = new OrderItem();
            orderItem.setCart(cart);
            orderItem.setProduct(product);
            orderItem.setPrice(product.getPrice());
            orderItem.setQuantity(cartRequest.getQuantity() != null ? cartRequest.getQuantity() : 1);
            orderItemRepository.save(orderItem);
        } else {
            // update số lượng sản phẩm tỏng giỏ hàng
            Integer newQuantity = cartRequest.getQuantity() != null ? cartRequest.getQuantity() : orderItem.getQuantity() + 1;
            if (newQuantity <= 0) {
                orderItemRepository.delete(orderItem);
                cartRepository.delete(cart);
                return null;
            }
            orderItem.setQuantity(newQuantity);
        }
        orderItemRepository.save(orderItem);
        return cart;
    }

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