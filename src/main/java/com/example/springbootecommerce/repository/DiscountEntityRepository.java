package com.example.springbootecommerce.repository;

import com.example.springbootecommerce.model.DiscountEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscountEntityRepository extends JpaRepository<DiscountEntity, Integer> {
    Optional<Object> findByDiscountCode(String discountCode);

    Page<DiscountEntity> findByShopEntityId(Integer shopEntityId, Pageable pageable);
}
