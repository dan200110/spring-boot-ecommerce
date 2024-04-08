package com.example.springbootecommerce.repository;

import com.example.springbootecommerce.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderEntityRepository extends JpaRepository<OrderEntity, Integer> {
}
