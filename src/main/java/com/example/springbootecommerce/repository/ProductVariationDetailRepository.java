package com.example.springbootecommerce.repository;

import com.example.springbootecommerce.model.ProductVariationDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductVariationDetailRepository extends JpaRepository<ProductVariationDetailEntity, Integer> {
    List<ProductVariationDetailEntity> findByProductEntityId(int productId);
}