package com.example.be_deliveryfood.service;

import com.example.be_deliveryfood.dto.repository.FavoriteRepository;
import com.example.be_deliveryfood.dto.repository.ProductRepository;
import com.example.be_deliveryfood.dto.repository.UserRepository;
import com.example.be_deliveryfood.entity.Favorite;
import com.example.be_deliveryfood.entity.Order;
import com.example.be_deliveryfood.entity.Product;
import com.example.be_deliveryfood.entity.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    //Kiểm tra một sản phẩm có trong danh sách yêu thích không
    public boolean isFavorite(Long userId, Long productId) {
        return favoriteRepository.existsByUserIdAndProductId(userId, productId);
    }

    // Lấy tất cả các favorites
    public List<Favorite> getAllFavorites() {
        return favoriteRepository.findAll();
    }

    // Thêm một sản phẩm vào danh sách yêu thích
    @Transactional
    public Favorite addToFavorites(Long userId, Long productId) {
        // Kiểm tra xem đã có trong favorites chưa
        if (favoriteRepository.existsByUserIdAndProductId(userId, productId)) {
            throw new ValidationException("Product is already in favorites");
        }

        // Tìm user và product
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));

        // Tạo mới favorite
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setProduct(product);
        favorite.setFavorite(true);

        return favoriteRepository.save(favorite);
    }

    // Lấy danh sách sản phẩm yêu thích của một user
    public List<Favorite> getFavorites(Long userId) {
        // Kiểm tra user có tồn tại không
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found with id: " + userId);
        }
        return favoriteRepository.findByUserId(userId);
    }

//    Lấy danh sách sản phẩm yêu thích của người dùng
@Transactional
public List<Product> getProductsByUserId(Long userId) {
    if (!userRepository.existsById(userId)) {
        throw new EntityNotFoundException("User not found with id: " + userId);
    }

    List<Favorite> favorites = favoriteRepository.findByUserId(userId);

    // Truy cập các thuộc tính để đảm bảo dữ liệu được tải đầy đủ
    List<Product> products = new ArrayList<>();
    for (Favorite favorite : favorites) {
        Product product = favorite.getProduct();
        // Truy cập các thuộc tính của Product để khởi tạo
        product.getId();
        product.getName();
        product.getPrice();
        product.getDescription();
        product.getCategory();
        product.getImage();
        product.getAvailable();
        products.add(product);
    }

    return products;
}

    // Xóa một sản phẩm khỏi danh sách yêu thích
    @Transactional
    public void removeFromFavorites(Long userId, Long productId) {
        Favorite favorite = favoriteRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new EntityNotFoundException("Product is not in favorites"));

        favoriteRepository.delete(favorite);
    }
}