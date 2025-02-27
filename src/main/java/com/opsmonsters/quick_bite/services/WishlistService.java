package com.opsmonsters.quick_bite.services;

import com.opsmonsters.quick_bite.models.Product;
import com.opsmonsters.quick_bite.models.Users;
import com.opsmonsters.quick_bite.repositories.ProductRepo;
import com.opsmonsters.quick_bite.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class WishlistService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductRepo productRepo;


    public Set<Product> getWishlist(String userEmail) {
        Users user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getWishlist();
    }


    public String addToWishlist(String userEmail, Long productId) {
        Users user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        user.getWishlist().add(product);
        userRepo.save(user);
        return "Product added to wishlist";
    }


    public String removeFromWishlist(String userEmail, Long productId) {
        Users user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        user.getWishlist().remove(product);
        userRepo.save(user);
        return "Product removed from wishlist";
    }
}