package com.example.be_deliveryfood.service;

import com.example.be_deliveryfood.dto.repository.ProductRepository;
import com.example.be_deliveryfood.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> searchProductsbyName(String keyword){
        System.out.println("Searching for: " + keyword);
        List<Product> products = productRepository.findByNameContainingIgnoreCase(keyword);
        System.out.println("Found products: " + products.size());
        return products;

    }
    //// search theo danh mục
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    //// Phương thức mới: Lấy danh sách danh mục
    public List<String> getAllCategories() {
        return productRepository.findAll()
                .stream()
                .map(Product::getCategory)
                .distinct()
                .collect(Collectors.toList());
    }

}
