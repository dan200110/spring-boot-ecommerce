package com.example.springbootecommerce.repository;

import com.example.springbootecommerce.model.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductEntityRepository extends JpaRepository<ProductEntity, Integer> {
    Page<ProductEntity> findByIsDraftFalse(Pageable pageable);

    Page<ProductEntity> findByIsDraftTrue(Pageable pageable);

    List<ProductEntity> findByShopEntityId(int shopId);
}
