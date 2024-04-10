package com.example.springbootecommerce.service.implementations;

import com.example.springbootecommerce.dto.productentity.ProductEntityAfterCreatedDto;
import com.example.springbootecommerce.dto.productentity.ProductEntityCreateDto;
import com.example.springbootecommerce.dto.productentity.ProductEntityDetailDto;
import com.example.springbootecommerce.dto.productentity.ProductEntityIndexDto;
import com.example.springbootecommerce.dto.productentity.productvariationentity.ProductVariationDetailDto;
import com.example.springbootecommerce.dto.productentity.productvariationentity.ProductVariationNameDto;
import com.example.springbootecommerce.dto.productentity.productvariationentity.ProductVariationValueDto;
import com.example.springbootecommerce.exception.ResourceNotFoundException;
import com.example.springbootecommerce.mapper.productentity.ProductMapper;
import com.example.springbootecommerce.mapper.productentity.productvariation.ProductVariationMapper;
import com.example.springbootecommerce.model.*;
import com.example.springbootecommerce.repository.ProductEntityRepository;
import com.example.springbootecommerce.repository.ProductVariationDetailRepository;
import com.example.springbootecommerce.repository.ProductVariationNameRepository;
import com.example.springbootecommerce.repository.ProductVariationValueRepository;
import com.example.springbootecommerce.service.interfaces.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final FileStorageServiceInterface fileStorageServiceInterface;
    private final FileAwsServiceInterface fileAwsServiceInterface;

    @Override
    public Page<ProductEntityIndexDto> getAllProducts(Pageable pageable) {
        Page<ProductEntity> productEntityPage = productRepository.findByIsDraftFalse(pageable);
        return productEntityPage.map(productMapper::toIndexDto);
    }

    @Override
    public Page<ProductEntityIndexDto> getAllDraftProducts(Pageable pageable) {
        Page<ProductEntity> productEntityPage = productRepository.findByIsDraftTrue(pageable);
        return productEntityPage.map(productMapper::toIndexDto);
    }

    @Override
    public void uploadProductImage(int productId, MultipartFile productImage) {


        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        // Upload main product image
        if (productImage != null && !productImage.isEmpty()) {
            try {
                // Save the uploaded image
                String productThumb = fileAwsServiceInterface.uploadFile(productImage);
                productEntity.setProductThumb(productThumb);
                productRepository.save(productEntity);
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload product image: " + e.getMessage());
            }
        } else {
            throw new RuntimeException("Product image is empty or null.");
        }
    }

    @Override
    public ResponseEntity<String> publishProduct(int productId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        if (!product.isDraft()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product is already published");
        }

        product.setDraft(false);
        productRepository.save(product);

        return ResponseEntity.ok("Product published successfully");
    }

    @Override
    public ResponseEntity<String> unPublishProduct(int productId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        if (product.isDraft()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product is already unpublished");
        }

        product.setDraft(true);
        productRepository.save(product);

        return ResponseEntity.ok("Product unpublished successfully");
    }

    @Override
    @Transactional
    public ProductEntityAfterCreatedDto createProduct(ProductEntityCreateDto productEntityCreateDto) {
        validateProductEntityCreateDto(productEntityCreateDto);
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

        // Calculate total quantity
        int totalQuantity = variationDetailEntities.stream()
                .mapToInt(ProductVariationDetailEntity::getQuantity)
                .sum();

        // Calculate default price
        int defaultPrice = variationDetailEntities.stream()
                .mapToInt(ProductVariationDetailEntity::getPrice)
                .min()
                .orElse(0);

        // Update productEntity with totalQuantity and defaultPrice
        productEntity.setTotalQuantity(totalQuantity);
        productEntity.setDefaultPrice(defaultPrice);

        // Save updated productEntity
        productEntity = productRepository.save(productEntity);

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
        detailEntity.setSku(detailDto.getSku());
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

    @Override
    public ProductEntityDetailDto getProductById(int productId) {
        // Find product by ID
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find product with id " + productId));

        // Map product entity to detail DTO


        ProductEntityDetailDto productEntityDetailDto = productMapper.toDetailDto(productEntity);

        // Find product variation names by product ID
        List<ProductVariationNameEntity> productVariationNameEntities = productVariationNameRepository.findByProductEntityId(productId);
        List<ProductVariationValueEntity> productVariationValueEntities = productVariationValueRepository.findByProductEntityId(productId);
        List<ProductVariationDetailEntity> productVariationDetailEntities = productVariationDetailRepository.findByProductEntityId(productId);

        List<ProductVariationNameDto> productVariationNameDtos = productVariationMapper.toVariationNameDtos(productVariationNameEntities);
        List<ProductVariationValueDto> productVariationValueDtos = productVariationMapper.toVariationValueDtos(productVariationValueEntities);
        List<ProductVariationDetailDto> productVariationDetailDtos = productVariationMapper.toVariationDetailDtos(productVariationDetailEntities);

        productEntityDetailDto.setProductVariationNames(productVariationNameDtos);
        productEntityDetailDto.setProductVariationValues(productVariationValueDtos);
        productEntityDetailDto.setProductVariationDetails(productVariationDetailDtos);
        return productEntityDetailDto;
    }


    private void validateProductEntityCreateDto(ProductEntityCreateDto productEntityCreateDto) {
        if (!isValidCategoryId(productEntityCreateDto.getCategoryId())) {
            throw new IllegalArgumentException("Invalid categoryId");
        }
        if (!isValidSupplierId(productEntityCreateDto.getSupplierId())) {
            throw new IllegalArgumentException("Invalid supplierId");
        }
        if (!isValidUserId(productEntityCreateDto.getUserId())) {
            throw new IllegalArgumentException("Invalid userId");
        }
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
