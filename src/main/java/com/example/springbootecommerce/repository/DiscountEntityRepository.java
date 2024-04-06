package com.example.springbootecommerce.repository;

import com.example.springbootecommerce.model.DiscountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountEntityRepository extends JpaRepository<DiscountEntity, Integer> {
}
