package com.example.springbootecommerce.repository;

import com.example.springbootecommerce.model.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductEntityRepository extends JpaRepository<ProductEntity, Integer> {
    Page<ProductEntity> findByIsDraftFalse(Pageable pageable);

    Page<ProductEntity> findByIsDraftTrue(Pageable pageable);

}
