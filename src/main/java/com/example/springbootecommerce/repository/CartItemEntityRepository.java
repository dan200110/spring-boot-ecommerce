package com.example.springbootecommerce.repository;

import com.example.springbootecommerce.model.CartItemEntity;
import com.example.springbootecommerce.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemEntityRepository extends JpaRepository<CartItemEntity, Integer> {
    List<CartItemEntity> findByUserEntityId(int userId);

    Optional<CartItemEntity> findByIdAndUserEntity(int cartItemId, UserEntity userEntity);
}
