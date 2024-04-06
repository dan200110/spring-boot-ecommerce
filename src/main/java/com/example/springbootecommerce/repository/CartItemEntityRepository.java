package com.example.springbootecommerce.repository;

import com.example.springbootecommerce.model.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemEntityRepository extends JpaRepository<CartItemEntity, Integer> {
    List<CartItemEntity> findByUserEntityId(int userId);
}
