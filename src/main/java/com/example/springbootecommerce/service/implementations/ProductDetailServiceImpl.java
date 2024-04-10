package com.example.springbootecommerce.service.implementations;

import com.example.springbootecommerce.dto.productentity.productvariationentity.ProductVariationDetailIndexDto;
import com.example.springbootecommerce.exception.ResourceNotFoundException;
import com.example.springbootecommerce.mapper.productentity.productvariation.ProductVariationMapper;
import com.example.springbootecommerce.model.FileInfo;
import com.example.springbootecommerce.model.ProductVariationDetailEntity;
import com.example.springbootecommerce.repository.ProductVariationDetailRepository;
import com.example.springbootecommerce.service.interfaces.FileAwsServiceInterface;
import com.example.springbootecommerce.service.interfaces.FileStorageServiceInterface;
import com.example.springbootecommerce.service.interfaces.ProductDetailServiceInterface;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Getter
@Setter
@RequiredArgsConstructor
@Transactional
public class ProductDetailServiceImpl implements ProductDetailServiceInterface {
    private final ProductVariationDetailRepository productVariationDetailRepository;
    private final FileStorageServiceInterface fileStorageServiceInterface;
    private final ProductVariationMapper productVariationMapper;
    private final FileAwsServiceInterface fileAwsServiceInterface;

    @Override
    public void uploadProductDetailImage(int productDetailId, MultipartFile productDetailImage) {
        ProductVariationDetailEntity productVariationDetailEntity = productVariationDetailRepository.findById(productDetailId)
                .orElseThrow(() -> new ResourceNotFoundException("Product detail not found with id: " + productDetailId));

        // Upload main product image
        if (productDetailImage != null && !productDetailImage.isEmpty()) {
            try {
                String productThumb = fileAwsServiceInterface.uploadFile(productDetailImage);
                productVariationDetailEntity.setImage(productThumb);
                productVariationDetailRepository.save(productVariationDetailEntity);
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload product detail image: " + e.getMessage());
            }
        } else {
            throw new RuntimeException("Product detail image is empty or null.");
        }
    }

    @Override
    public List<ProductVariationDetailIndexDto> getAllProductDetailByProductId(int productId) {
        List<ProductVariationDetailEntity> productVariationDetailEntities = productVariationDetailRepository.findByProductEntityId(productId);
        // Detach ProductEntity objects to avoid serialization issues
        for (ProductVariationDetailEntity detailEntity : productVariationDetailEntities) {
            Hibernate.initialize(detailEntity.getProductEntity());
            Hibernate.unproxy(detailEntity.getProductEntity());
        }

        return productVariationMapper.toVariationDetailIndexDtos(productVariationDetailEntities);
    }
}
