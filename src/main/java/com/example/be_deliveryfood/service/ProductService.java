package com.example.be_deliveryfood.service;

import com.example.be_deliveryfood.entity.Product;
import com.example.be_deliveryfood.dto.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
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

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product product = getProductById(id);
        product.setImage(productDetails.getImage());
        product.setName(productDetails.getName());
        product.setCategory(productDetails.getCategory());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setAvailable(productDetails.getAvailable());
        return productRepository.save(product);
    }

    public List<String> getAllCategories() {
        return productRepository.findAll()
                .stream()
                .map(Product::getCategory)
                .distinct()
                .collect(Collectors.toList());
    }

    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }
}