package com.opsmonsters.quick_bite.repository;

import com.opsmonsters.quick_bite.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepo extends JpaRepository<ProductImage, Long> {

}