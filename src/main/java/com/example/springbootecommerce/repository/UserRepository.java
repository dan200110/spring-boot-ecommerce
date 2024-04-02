package com.example.springbootecommerce.repository;

import com.example.springbootecommerce.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findUserByUsername(String username);
    Boolean existsByUsername(String username);
}
