package com.opsmonsters.quick_bite.services;

import com.opsmonsters.quick_bite.dto.ProductDto;
import com.opsmonsters.quick_bite.models.Product;
import com.opsmonsters.quick_bite.models.Tag;
import com.opsmonsters.quick_bite.repositories.ProductRepo;
import com.opsmonsters.quick_bite.repositories.TagRepo;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepo productRepo;
    private final TagRepo tagRepository;

    @Autowired
    public ProductService(ProductRepo productRepo, TagRepo tagRepository) {
        this.productRepo = productRepo;
        this.tagRepository = tagRepository;
    }

    @Transactional
    public ProductDto createProduct(ProductDto dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setImageUrl(dto.getImageUrl());
        product.setMrp(dto.getMrp());
        product.setDiscount(dto.getDiscount());
        product.setDescription(dto.getDescription());
        product.setAbout(dto.getAbout());
        product.setStock(dto.getStock());


        Product savedProduct = productRepo.save(product);


        Set<Tag> managedTags = manageTags(dto.getTags());
        savedProduct.setTags(managedTags);


        savedProduct = productRepo.save(savedProduct);

        return convertToDto(savedProduct);
    }


    public List<ProductDto> getAllProducts() {
        return productRepo.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ProductDto getProductById(Long productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product with ID " + productId + " not found"));
        return convertToDto(product);
    }

    public List<ProductDto> getProductsByIds(List<Long> productIds) {
        List<Product> products = productRepo.findAllById(productIds);
        return products.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Transactional
    public ProductDto updateProduct(Long productId, ProductDto dto) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product with ID " + productId + " not found"));

        product.setName(dto.getName());
        product.setImageUrl(dto.getImageUrl());
        product.setMrp(dto.getMrp());
        product.setDiscount(dto.getDiscount());
        product.setDescription(dto.getDescription());
        product.setAbout(dto.getAbout());
        product.setStock(dto.getStock());


        Set<Tag> managedTags = manageTags(dto.getTags());
        product.setTags(managedTags);

        Product updatedProduct = productRepo.save(product);
        return convertToDto(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long productId) {
        if (!productRepo.existsById(productId)) {
            throw new RuntimeException("Product with ID " + productId + " not found");
        }
        productRepo.deleteById(productId);
    }

    @Transactional
    public ProductDto updateStock(Long productId, int newStock) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product with ID " + productId + " not found"));

        product.setStock(newStock);
        Product updatedProduct = productRepo.save(product);

        return convertToDto(updatedProduct);
    }


    private ProductDto convertToDto(Product product) {
        return new ProductDto(
                product.getProductId(),
                product.getName(),
                product.getImageUrl(),
                product.getMrp(),
                product.getDiscount(),
                product.getDescription(),
                product.getAbout(),
                product.getTags(),
                product.getStock()
        );
    }


    private Set<Tag> manageTags(Set<Tag> tags) {
        if (tags == null || tags.isEmpty()) return new HashSet<>();

        Set<Tag> managedTags = new HashSet<>();

        for (Tag tag : tags) {
            Optional<Tag> existingTag = tagRepository.findByName(tag.getName());
            if (existingTag.isPresent()) {
                managedTags.add(existingTag.get());
            } else {
                managedTags.add(tagRepository.save(tag));
            }
        }

        return managedTags;
    }

}
