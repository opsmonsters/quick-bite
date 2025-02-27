//package com.opsmonsters.quick_bite.services;
//
//import com.opsmonsters.quick_bite.dto.ProductDto;
//import com.opsmonsters.quick_bite.models.Product;
//import com.opsmonsters.quick_bite.repositories.ProductRepo;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.HashSet;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@Service
//public class WishlistUnauthorizedService {
//
//    @Autowired
//    private ProductRepo productRepo;
//
//    // Fetch wishlist and return as DTOs
//    public Set<ProductDto> getWishlist(HttpSession session) {
//        Set<Product> wishlist = (Set<Product>) session.getAttribute("guest_wishlist");
//        if (wishlist == null) {
//            return new HashSet<>();
//        }
//
//
//        return wishlist.stream()
//                .map(product -> new ProductDto(
//                        product.getProductId(),
//                        product.getName(),
//                        product.getMrp(),
//                        product.getDiscount(),
//                        product.getDescription(),
//                        product.getAbout()
//                ))
//                .collect(Collectors.toSet());
//    }
//
//    public String addToWishlist(Long productId, HttpSession session) {
//        Product product = productRepo.findById(productId)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//
//        Set<Product> guestWishlist = (Set<Product>) session.getAttribute("guest_wishlist");
//        if (guestWishlist == null) {
//            guestWishlist = new HashSet<>();
//        }
//        guestWishlist.add(product);
//        session.setAttribute("guest_wishlist", guestWishlist);
//        return "Product added to guest wishlist";
//    }
//
//    public String removeFromWishlist(Long productId, HttpSession session) {
//        Product product = productRepo.findById(productId)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//
//        Set<Product> guestWishlist = (Set<Product>) session.getAttribute("guest_wishlist");
//        if (guestWishlist != null) {
//            guestWishlist.remove(product);
//            session.setAttribute("guest_wishlist", guestWishlist);
//            return "Product removed from guest wishlist";
//        }
//        return "No guest wishlist found";
//    }
//}
