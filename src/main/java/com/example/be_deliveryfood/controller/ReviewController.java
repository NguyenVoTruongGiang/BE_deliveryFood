package com.example.be_deliveryfood.controller;

import com.example.be_deliveryfood.entity.Review;
import com.example.be_deliveryfood.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody Map<String, Object> request) {
        Long userId = Long.parseLong(request.get("user_id").toString());
        Long productId = Long.parseLong(request.get("product_id").toString());
        Integer rating = Integer.parseInt(request.get("rating").toString());
        String comment = request.get("comment") != null ? request.get("comment").toString() : null;
        Review review = reviewService.createReview(userId, productId, rating, comment);
        return ResponseEntity.ok(review);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getReviewsByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getReviewsByProductId(productId));
    }
}