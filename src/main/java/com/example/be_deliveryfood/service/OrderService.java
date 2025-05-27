package com.example.be_deliveryfood.service;

import com.example.be_deliveryfood.dto.request.OrderRequest;
import com.example.be_deliveryfood.dto.request.OrderItemRequest;
import com.example.be_deliveryfood.entity.Cart;
import com.example.be_deliveryfood.entity.Order;
import com.example.be_deliveryfood.entity.OrderItem;
import com.example.be_deliveryfood.entity.Product;
import com.example.be_deliveryfood.entity.User;
import com.example.be_deliveryfood.dto.repository.CartRepository;
import com.example.be_deliveryfood.dto.repository.OrderItemRepository;
import com.example.be_deliveryfood.dto.repository.OrderRepository;
import com.example.be_deliveryfood.dto.repository.ProductRepository;
import com.example.be_deliveryfood.dto.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartRepository cartRepository;

    @Transactional
    public Order createOrder(OrderRequest orderRequest, Long userId) {
        // Lấy thông tin người dùng
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        // Lấy thông tin giỏ hàng nếu có
        Cart cart = null;
        if (orderRequest.getCart_id() != null) {
            cart = cartRepository.findById(orderRequest.getCart_id())
                    .orElseThrow(() -> new EntityNotFoundException("Cart not found with id: " + orderRequest.getCart_id()));
        }

        // Tạo đơn hàng mới
        Order order = new Order();
        order.setUser(user);
        order.setCart(cart);
        order.setDelivery_address(orderRequest.getDelivery_address());
        order.setStatus(Order.OrderStatus.PENDING);
        order.setCreated_at(LocalDateTime.now());

        // Lưu đơn hàng trước để có ID
        Order savedOrder = orderRepository.save(order);

        // Thêm các mục đơn hàng
        if (orderRequest.getItems() != null && !orderRequest.getItems().isEmpty()) {
            for (OrderItemRequest itemRequest : orderRequest.getItems()) {
                Product product = productRepository.findById(itemRequest.getProduct_id())
                        .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + itemRequest.getProduct_id()));

                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(savedOrder);
                orderItem.setProduct(product);
                orderItem.setQuantity(itemRequest.getQuantity());
                orderItem.setPrice(product.getPrice());

                savedOrder.addOrderItem(orderItem);
            }
        }

        // Tính tổng giá trị đơn hàng
        savedOrder.calculateTotalPrice();

        // Lưu lại đơn hàng sau khi cập nhật
        return orderRepository.save(savedOrder);
    }

    @Transactional(readOnly = true)
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersByUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        return orderRepository.findByUserId(userId);
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));

        order.getStatus();
        return orderRepository.save(order);
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));

        // Chỉ có thể hủy đơn hàng khi đang ở trạng thái PENDING
        if (order.getStatus()  == Order.OrderStatus.PENDING) {
            order.setStatus(Order.OrderStatus.CANCELLED);
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Cannot cancel order with status: " + order.getStatus());
        }
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}