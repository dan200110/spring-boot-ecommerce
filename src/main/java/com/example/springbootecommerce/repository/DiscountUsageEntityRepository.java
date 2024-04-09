package com.example.springbootecommerce.repository;

import com.example.springbootecommerce.model.DiscountEntity;
import com.example.springbootecommerce.model.DiscountUsageEntity;
import com.example.springbootecommerce.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountUsageEntityRepository extends JpaRepository<DiscountUsageEntity, Integer> {
    DiscountUsageEntity findByDiscountEntityAndUserEntity(DiscountEntity discountEntity, UserEntity userEntity);
}
