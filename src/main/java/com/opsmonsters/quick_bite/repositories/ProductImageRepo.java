package com.opsmonsters.quick_bite.repositories;

import com.opsmonsters.quick_bite.models.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepo extends JpaRepository<ProductImage, Long> {

}