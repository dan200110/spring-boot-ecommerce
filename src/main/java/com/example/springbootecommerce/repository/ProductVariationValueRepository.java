package com.example.springbootecommerce.repository;

import com.example.springbootecommerce.model.ProductVariationValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductVariationValueRepository extends JpaRepository<ProductVariationValueEntity, Integer> {
    Optional<ProductVariationValueEntity> findByProductVariationValueUuid(UUID productVariationValueUuid);
}
