package com.example.springbootecommerce.repository;

import com.example.springbootecommerce.model.ProductCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryEntityRepository extends JpaRepository<ProductCategoryEntity, Integer> {

    Boolean existsByName(String name);
}