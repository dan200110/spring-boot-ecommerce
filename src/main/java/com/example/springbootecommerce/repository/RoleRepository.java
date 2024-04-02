package com.example.springbootecommerce.repository;

import com.example.springbootecommerce.model.RoleEntity;
import com.example.springbootecommerce.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity findRoleByName(String name);
}
