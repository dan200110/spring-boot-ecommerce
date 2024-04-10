package com.example.springbootecommerce.repository;

import com.example.springbootecommerce.model.ProductVariationDetailEntity;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductVariationDetailRepository extends JpaRepository<ProductVariationDetailEntity, Integer> {
    List<ProductVariationDetailEntity> findByProductEntityId(int productId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value ="10000")})
    ProductVariationDetailEntity findWithLockingById(int id);
}