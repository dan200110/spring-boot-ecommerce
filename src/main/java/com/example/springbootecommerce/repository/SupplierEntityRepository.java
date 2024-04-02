package com.example.springbootecommerce.repository;

import com.example.springbootecommerce.model.SupplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierEntityRepository extends JpaRepository<SupplierEntity, Integer> {

    Boolean existsByName(String name);
}
