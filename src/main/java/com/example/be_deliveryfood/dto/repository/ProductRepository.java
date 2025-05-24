package com.example.be_deliveryfood.dto.repository;

import com.example.be_deliveryfood.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);
    List<Product> findByAvailableTrue();
}