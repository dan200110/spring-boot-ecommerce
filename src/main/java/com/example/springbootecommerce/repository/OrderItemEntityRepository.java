package com.example.springbootecommerce.repository;

import com.example.springbootecommerce.model.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemEntityRepository extends JpaRepository<OrderItemEntity, Integer> {
}
