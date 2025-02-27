package com.opsmonsters.quick_bite.controller;

import com.opsmonsters.quick_bite.dto.CartDetailsDto;
import com.opsmonsters.quick_bite.dto.CartDto;
import com.opsmonsters.quick_bite.dto.ResponseDto;
import com.opsmonsters.quick_bite.services.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseDto> addToCart(@RequestBody CartDto cartDto) {
        ResponseDto response = cartService.addToCart(cartDto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/remove/{cartDetailsId}")
    public ResponseEntity<String> removeFromCart(@PathVariable Long cartDetailsId) {
        String response = cartService.removeFromCart(cartDetailsId);
        return response.equals("Product removed from cart!")
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(404).body(response);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<?> getCartDetails(@PathVariable Long cartId) {
        CartDetailsDto cartDetails = cartService.getCartDetails(cartId);
        return (cartDetails == null)
                ? ResponseEntity.status(404).body("Cart not found!")
                : ResponseEntity.ok(cartDetails);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserCarts(@PathVariable Long userId) {
        List<CartDto> userCarts = cartService.getUserCarts(userId);
        return userCarts.isEmpty()
                ? ResponseEntity.status(404).body("No carts found for this user!")
                : ResponseEntity.ok(userCarts);
    }

    @PutMapping("/{cartId}/checkout")
    public ResponseEntity<String> checkoutCart(@PathVariable Long cartId) {
        String response = cartService.checkoutCart(cartId);
        return response.equals("Cart checked out successfully!")
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(404).body(response);
    }
}
