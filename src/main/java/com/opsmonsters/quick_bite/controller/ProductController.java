package com.opsmonsters.quick_bite.controller;

import com.opsmonsters.quick_bite.dto.ProductDto;
import com.opsmonsters.quick_bite.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/admin/products")
    public ResponseEntity<String> createProduct(@RequestBody ProductDto dto) {
        productService.createProduct(dto);
        return new ResponseEntity<>("Product posted successfully", HttpStatus.CREATED);
    }

    @GetMapping("/users/products")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/users/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("productId") Long productId) {
        ProductDto product = productService.getProductById(productId);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<String> updateProduct(@PathVariable("productId") Long productId, @RequestBody ProductDto dto) {
        productService.updateProduct(productId, dto);
        return new ResponseEntity<>("Product updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/admin/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable("productId") Long productId) {
        productService.deleteProduct(productId);
        return new ResponseEntity<>("Product deleted successfully", HttpStatus.NO_CONTENT);
    }

    @PutMapping("/admin/products/{productId}/stock")
    public ResponseEntity<ProductDto> updateStock(@PathVariable Long productId, @RequestParam int newStock) {
        ProductDto updatedProduct = productService.updateStock(productId, newStock);
        return ResponseEntity.ok(updatedProduct);
    }
}
     