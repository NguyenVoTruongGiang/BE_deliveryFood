package com.example.be_deliveryfood.controller;

import com.example.be_deliveryfood.dto.request.FavoriteRequest;
import com.example.be_deliveryfood.entity.Favorite;
import com.example.be_deliveryfood.entity.Product;
import com.example.be_deliveryfood.entity.User;
import com.example.be_deliveryfood.service.FavoriteService;
import com.example.be_deliveryfood.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private UserService userService;

    // Kiểm tra sản phẩm có trong danh sách yêu thích không
    @GetMapping("/check/{productId}")
    public ResponseEntity<Boolean> checkIsFavorite(@PathVariable Long productId) {
        System.out.println("checkIsFavorite: " + productId );
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new ValidationException("User not found"));

        boolean isFavorite = favoriteService.isFavorite(user.getId(), productId);
        return ResponseEntity.ok(isFavorite);
    }

    //Thêm sản phẩm vào danh sách yêu thích
    @PostMapping("/add")
    public ResponseEntity<Favorite> addToFavorites(@Valid @RequestBody FavoriteRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new ValidationException("User not found"));

        Favorite favorite = favoriteService.addToFavorites(user.getId(), request.getProduct_id());
        return ResponseEntity.ok(favorite);
    }

    // Lấy tất cả favorites
    @GetMapping
    public ResponseEntity<List<Favorite>> getAllFavorites() {
        return ResponseEntity.ok(favoriteService.getAllFavorites());
    }

   // Kiểm tra sản phẩm có trong danh sách yêu thích không
   @GetMapping("/take-product")
   public ResponseEntity<List<Product>> getFavoriteProducts() {
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       String email = auth.getName();
       User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new ValidationException("User not found"));

       List<Product> favoriteProducts = favoriteService.getProductsByUserId(user.getId());
       return ResponseEntity.ok(favoriteProducts);
   }

    // Xóa sản phẩm khỏi danh sách yêu thích
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<Void> removeFromFavorites(@PathVariable Long productId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new ValidationException("User not found"));

        favoriteService.removeFromFavorites(user.getId(), productId);
        return ResponseEntity.noContent().build();
    }
}