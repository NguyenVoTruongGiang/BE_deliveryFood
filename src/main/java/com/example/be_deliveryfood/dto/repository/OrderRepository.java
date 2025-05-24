package com.example.be_deliveryfood.dto.repository;

import com.example.be_deliveryfood.entity.Order;
import com.example.be_deliveryfood.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
