package com.example.springbootecommerce.service.implementations;

import com.example.springbootecommerce.dto.productentity.ProductEntityAfterCreatedDto;
import com.example.springbootecommerce.dto.productentity.ProductEntityCreateDto;
import com.example.springbootecommerce.dto.productentity.ProductEntityIndexDto;
import com.example.springbootecommerce.dto.productentity.productvariationentity.ProductVariationDetailDto;
import com.example.springbootecommerce.dto.productentity.productvariationentity.ProductVariationNameDto;
import com.example.springbootecommerce.dto.productentity.productvariationentity.ProductVariationValueDto;
import com.example.springbootecommerce.mapper.productentity.ProductMapper;
import com.example.springbootecommerce.mapper.productentity.productvariation.ProductVariationMapper;
import com.example.springbootecommerce.model.*;
import com.example.springbootecommerce.repository.ProductEntityRepository;
import com.example.springbootecommerce.repository.ProductVariationDetailRepository;
import com.example.springbootecommerce.repository.ProductVariationNameRepository;
import com.example.springbootecommerce.repository.ProductVariationValueRepository;
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

import java.util.List;
import java.util.stream.Collectors;

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
    private final ProductVariationMapper productVariationMapper;
    private final ProductVariationNameRepository productVariationNameRepository;
    private final ProductVariationValueRepository productVariationValueRepository;
    private final ProductVariationDetailRepository productVariationDetailRepository;

    @Override
    public Page<ProductEntityIndexDto> getAllProducts(Pageable pageable) {
        Page<ProductEntity> productEntityPage = productRepository.findAll(pageable);
        return productEntityPage.map(productMapper::toIndexDto);
    }

    @Override
    @Transactional
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

        // Map productEntity and save
        ProductEntity productEntity = productMapper.toEntity(productEntityCreateDto);
        productEntity = productRepository.save(productEntity);

        // Map product variation names and save each one
        ProductEntity finalProductEntity = productEntity;
        List<ProductVariationNameEntity> variationNameEntities = productEntityCreateDto.getProductVariationNames()
                .stream()
                .map(nameDto -> {
                    ProductVariationNameEntity nameEntity = toVariationNameEntity(nameDto, finalProductEntity);
                    return productVariationNameRepository.save(nameEntity);
                })
                .collect(Collectors.toList());

        // Now you can map and save product variation values and details in a similar manner

        // Map product variation values and save each one
        List<ProductVariationValueEntity> variationValueEntities = productEntityCreateDto.getProductVariationValues()
                .stream()
                .map(valueDto -> {
                    ProductVariationValueEntity valueEntity = toVariationValueEntity(valueDto, finalProductEntity);
                    return productVariationValueRepository.save(valueEntity);
                })
                .collect(Collectors.toList());

        // Map product variation details and save each one
        List<ProductVariationDetailEntity> variationDetailEntities = productEntityCreateDto.getProductVariationDetails()
                .stream()
                .map(detailDto -> {
                    ProductVariationDetailEntity detailEntity = toVariationDetailEntity(detailDto, finalProductEntity);
                    return productVariationDetailRepository.save(detailEntity);
                })
                .collect(Collectors.toList());

        return productMapper.toAfterCreatedDto(productEntity);
    }


    public ProductVariationNameEntity toVariationNameEntity(ProductVariationNameDto nameDto, ProductEntity productEntity) {
        ProductVariationNameEntity variationNameEntity = new ProductVariationNameEntity();
        variationNameEntity.setName(nameDto.getName());
        variationNameEntity.setProductVariationNameUuid(nameDto.getProductVariationNameUuid());
        variationNameEntity.setDescription(nameDto.getDescription());
        variationNameEntity.setProductEntity(productEntity);
        return variationNameEntity;
    }

    public ProductVariationValueEntity toVariationValueEntity(ProductVariationValueDto valueDto, ProductEntity productEntity) {
        ProductVariationValueEntity valueEntity = new ProductVariationValueEntity();
        valueEntity.setProductVariationValueUuid(valueDto.getProductVariationValueUuid());
        valueEntity.setValue(valueDto.getValue());
        // Find and set the corresponding ProductVariationNameEntity based on UUID
        ProductVariationNameEntity nameEntity = productVariationNameRepository.findByProductVariationNameUuid(valueDto.getProductVariationNameUuid())
                .orElseThrow(() -> new IllegalArgumentException("Invalid productVariationNameUuid"));
        valueEntity.setProductVariationNameEntity(nameEntity);
        valueEntity.setProductEntity(productEntity);
        return valueEntity;
    }

    public ProductVariationDetailEntity toVariationDetailEntity(ProductVariationDetailDto detailDto, ProductEntity productEntity) {
        ProductVariationDetailEntity detailEntity = new ProductVariationDetailEntity();
        detailEntity.setPrice(detailDto.getPrice());
        detailEntity.setQuantity(detailDto.getQuantity());
        detailEntity.setImage(detailDto.getImage());
        // Find and set the corresponding ProductVariationValueEntity based on UUID for variationValueFirst
        ProductVariationValueEntity firstValueEntity = productVariationValueRepository.findByProductVariationValueUuid(detailDto.getVariationValueFirstUuid())
                .orElseThrow(() -> new IllegalArgumentException("Invalid variationValueFirstUuid"));
        detailEntity.setVariationValueFirst(firstValueEntity);
        // Find and set the corresponding ProductVariationValueEntity based on UUID for variationValueSecond
        ProductVariationValueEntity secondValueEntity = productVariationValueRepository.findByProductVariationValueUuid(detailDto.getVariationValueSecondUuid())
                .orElseThrow(() -> new IllegalArgumentException("Invalid variationValueSecondUuid"));
        detailEntity.setVariationValueSecond(secondValueEntity);
        detailEntity.setProductEntity(productEntity);
        return detailEntity;
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
