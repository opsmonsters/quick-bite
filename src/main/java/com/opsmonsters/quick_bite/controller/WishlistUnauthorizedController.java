//package com.opsmonsters.quick_bite.controller;
//
//import com.opsmonsters.quick_bite.dto.ProductDto;
//import com.opsmonsters.quick_bite.services.WishlistUnauthorizedService;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Set;
//
//@RestController
//@RequestMapping("/wishlist")
//public class WishlistUnauthorizedController {
//
//    @Autowired
//    private WishlistUnauthorizedService wishlistunauthorizedService;
//
//
//    @GetMapping
//    public ResponseEntity<Set<ProductDto>> getWishlist(HttpSession session) {
//        Set<ProductDto> wishlist = wishlistunauthorizedService.getWishlist(session);
//        return ResponseEntity.ok(wishlist);
//    }
//
//    @PostMapping("/add/{productId}")
//    public ResponseEntity<String> addToWishlist(@PathVariable Long productId, HttpSession session) {
//        String message = wishlistunauthorizedService.addToWishlist(productId, session);
//        return ResponseEntity.ok(message);
//    }
//
//    @DeleteMapping("/remove/{productId}")
//    public ResponseEntity<String> removeFromWishlist(@PathVariable Long productId, HttpSession session) {
//        String message = wishlistunauthorizedService.removeFromWishlist(productId, session);
//        return ResponseEntity.ok(message);
//    }
//}
