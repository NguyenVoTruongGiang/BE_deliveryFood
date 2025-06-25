package com.example.be_deliveryfood.dto.repository;

import com.example.be_deliveryfood.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    boolean existsByUserIdAndProductId(Long userId, Long productId);
    List<Favorite> findByUserId(Long userId);
    List<Favorite> findByProductId(Long productId);
    Optional<Favorite> findByUserIdAndProductId(Long userId, Long productId);
}