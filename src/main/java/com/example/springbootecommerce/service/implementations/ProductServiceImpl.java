package com.example.springbootecommerce.service.implementations;

import com.example.springbootecommerce.dto.productentity.ProductEntityAfterCreatedDto;
import com.example.springbootecommerce.dto.productentity.ProductEntityCreateDto;
import com.example.springbootecommerce.dto.productentity.ProductEntityIndexDto;
import com.example.springbootecommerce.mapper.productentity.ProductMapper;
import com.example.springbootecommerce.model.ProductCategoryEntity;
import com.example.springbootecommerce.model.ProductEntity;
import com.example.springbootecommerce.model.SupplierEntity;
import com.example.springbootecommerce.model.UserEntity;
import com.example.springbootecommerce.repository.ProductEntityRepository;
import com.example.springbootecommerce.service.interfaces.CategoryServiceInterface;
import com.example.springbootecommerce.service.interfaces.ProductServiceInterface;
import com.example.springbootecommerce.service.interfaces.SupplierServiceInterface;
import com.example.springbootecommerce.service.interfaces.UserServiceInterface;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Getter
@Setter
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductServiceInterface {
    private final ProductEntityRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryServiceInterface categoryServiceInterface;
    private final SupplierServiceInterface supplierServiceInterface;
    private final UserServiceInterface userServiceInterface;

    @Override
    public Page<ProductEntityIndexDto> getAllProducts(Pageable pageable) {
        Page<ProductEntity> productEntityPage = productRepository.findAll(pageable);
        return productEntityPage.map(productMapper::toIndexDto);
    }

    @Override
    public ProductEntityAfterCreatedDto createProduct(ProductEntityCreateDto productEntityCreateDto) {
        if (!isValidCategoryId(productEntityCreateDto.getCategoryId())) {
            throw new IllegalArgumentException("Invalid categoryId");
        }
        if (!isValidSupplierId(productEntityCreateDto.getSupplierId())) {
            throw new IllegalArgumentException("Invalid supplierId");
        }
        if (!isValidUserId(productEntityCreateDto.getUserId())) {
            throw new IllegalArgumentException("Invalid userId");
        }

        ProductEntity productEntity = productMapper.toEntity(productEntityCreateDto);
        productEntity = productRepository.save(productEntity);
        return productMapper.toAfterCreatedDto(productEntity);
    }

    private boolean isValidCategoryId(int categoryId) {
        ProductCategoryEntity productCategoryEntity = categoryServiceInterface.getCategoryById(categoryId);
        return productCategoryEntity != null;
    }

    private boolean isValidSupplierId(int supplierId) {
        SupplierEntity supplierEntity = supplierServiceInterface.getSupplierById(supplierId);
        return supplierEntity != null;
    }

    private boolean isValidUserId(int userId) {
        UserEntity userEntity = userServiceInterface.findUserById(userId);
        return userEntity != null;
    }
}
