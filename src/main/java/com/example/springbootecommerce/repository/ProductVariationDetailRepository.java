package com.example.springbootecommerce.repository;

import com.example.springbootecommerce.model.ProductVariationDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductVariationDetailRepository extends JpaRepository<ProductVariationDetailEntity, Integer> {
}