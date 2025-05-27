//package com.example.be_deliveryfood.controller;
//
//import com.example.be_deliveryfood.dto.request.OrderRequest;
//import com.example.be_deliveryfood.entity.Order;
//import com.example.be_deliveryfood.entity.Order.OrderStatus;
//import com.example.be_deliveryfood.service.OrderService;
//import com.fasterxml.jackson.annotation.JsonView;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/order")
//public class OrderController {
//
//    @Autowired
//    private OrderService orderService;
//
//    @PostMapping("create")
//    public ResponseEntity<Order> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
//        // Lấy ID người dùng từ xác thực hiện tại
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        Long userId = Long.parseLong(auth.getName()); // Giả sử auth.getName() trả về userId dạng chuỗi
//
//        Order createdOrder = orderService.createOrder(orderRequest, userId);
//        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
//        Order order = orderService.getOrderById(id);
//        return ResponseEntity.ok(order);
//    }
//
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable Long userId) {
//        List<Order> orders = orderService.getOrdersByUser(userId);
//        return ResponseEntity.ok(orders);
//    }
//
//    @GetMapping("/my-orders")
//    public ResponseEntity<List<Order>> getCurrentUserOrders() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        Long userId = Long.parseLong(auth.getName()); // Giả sử auth.getName() trả về userId dạng chuỗi
//
//        List<Order> orders = orderService.getOrdersByUser(userId);
//        return ResponseEntity.ok(orders);
//    }
//
//    @PutMapping("/{id}/status")
//    public ResponseEntity<Order> updateOrderStatus(
//            @PathVariable Long id,
//            @RequestParam OrderStatus status) {
//        Order updatedOrder = orderService.updateOrderStatus(id, status);
//        return ResponseEntity.ok(updatedOrder);
//    }
//
////    @PostMapping("/{id}/cancel")
////    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
////        orderService.cancelOrder(id);
////        return ResponseEntity.noContent().build();
////    }
////
////    @GetMapping
////    public ResponseEntity<List<Order>> getAllOrders() {
////        List<Order> orders = orderService.getAllOrders();
////        return ResponseEntity.ok(orders);
////    }
//}