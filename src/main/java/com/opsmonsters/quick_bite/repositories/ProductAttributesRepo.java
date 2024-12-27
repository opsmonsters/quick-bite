package com.opsmonsters.quick_bite.repositories;

import com.opsmonsters.quick_bite.models.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductAttributesRepo extends JpaRepository<ProductAttribute, Long> {

}
