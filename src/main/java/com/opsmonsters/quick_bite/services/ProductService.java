package com.opsmonsters.quick_bite.services;

import com.opsmonsters.quick_bite.dto.ProductDto;
import com.opsmonsters.quick_bite.models.Product;
import com.opsmonsters.quick_bite.repositories.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    public ProductDto createProduct(ProductDto dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setMrp(dto.getMrp());
        product.setDiscount(dto.getDiscount());
        product.setDescription(dto.getDescription());
        product.setAbout(dto.getAbout());

        Product savedProduct = productRepo.save(product);

        dto.setProductId(savedProduct.getProductId());
        return dto;
    }

    public List<ProductDto> getAllProducts() {
        return productRepo.findAll()
                .stream()
                .map(product -> {
                    ProductDto dto = new ProductDto();
                    dto.setProductId(product.getProductId());
                    dto.setName(product.getName());
                    dto.setMrp(product.getMrp());
                    dto.setDiscount(product.getDiscount());
                    dto.setDescription(product.getDescription());
                    dto.setAbout(product.getAbout());
                    return dto;
                }).collect(Collectors.toList());
    }

    public ProductDto getProductById(Long productId) {
        Optional<Product> productOptional = productRepo.findById(productId);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            ProductDto dto = new ProductDto();
            dto.setProductId(product.getProductId());
            dto.setName(product.getName());
            dto.setMrp(product.getMrp());
            dto.setDiscount(product.getDiscount());
            dto.setDescription(product.getDescription());
            dto.setAbout(product.getAbout());
            return dto;
        } else {
            throw new RuntimeException("Product with ID " + productId + " not found");
        }
    }

    public ProductDto updateProduct(Long productId, ProductDto dto) {
        Optional<Product> productOptional = productRepo.findById(productId);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setName(dto.getName());
            product.setMrp(dto.getMrp());
            product.setDiscount(dto.getDiscount());
            product.setDescription(dto.getDescription());
            product.setAbout(dto.getAbout());

            Product updatedProduct = productRepo.save(product);

            dto.setProductId(updatedProduct.getProductId());
            return dto;
        } else {
            throw new RuntimeException("Product with ID " + productId + " not found");
        }
    }

    public void deleteProduct(Long productId) {
        productRepo.deleteById(productId);
    }
}
