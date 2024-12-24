package com.opsmonsters.quick_bite.repository;

import com.opsmonsters.quick_bite.model.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductAttributesRepo extends JpaRepository<ProductAttribute, Long> {

}
