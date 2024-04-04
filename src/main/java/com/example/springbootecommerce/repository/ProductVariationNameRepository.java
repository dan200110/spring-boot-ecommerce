package com.example.springbootecommerce.repository;

import com.example.springbootecommerce.model.ProductVariationNameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductVariationNameRepository extends JpaRepository<ProductVariationNameEntity, Integer> {
    Optional<ProductVariationNameEntity> findByProductVariationNameUuid(UUID productVariationNameUuid);
}
