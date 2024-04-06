package com.example.springbootecommerce.mapper.productentity.productvariation;

import com.example.springbootecommerce.dto.productentity.productvariationentity.ProductVariationDetailDto;
import com.example.springbootecommerce.dto.productentity.productvariationentity.ProductVariationDetailIndexDto;
import com.example.springbootecommerce.dto.productentity.productvariationentity.ProductVariationNameDto;
import com.example.springbootecommerce.dto.productentity.productvariationentity.ProductVariationValueDto;
import com.example.springbootecommerce.model.ProductVariationDetailEntity;
import com.example.springbootecommerce.model.ProductVariationNameEntity;
import com.example.springbootecommerce.model.ProductVariationValueEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductVariationMapper {
    default ProductVariationValueDto productVariationValueEntityToProductVariationValueDto(ProductVariationValueEntity productVariationValueEntity) {
        if (productVariationValueEntity == null) {
            return null;
        }

        return ProductVariationValueDto.builder()
                .productVariationValueUuid(productVariationValueEntity.getProductVariationValueUuid())
                .productVariationNameUuid(productVariationValueEntity.getProductVariationNameEntity() != null ? productVariationValueEntity.getProductVariationNameEntity().getProductVariationNameUuid() : null)
                .value(productVariationValueEntity.getValue())
                .build();
    }

    default ProductVariationDetailDto productVariationDetailEntityToProductVariationDetailDto(ProductVariationDetailEntity productVariationDetailEntity) {
        if (productVariationDetailEntity == null) {
            return null;
        }

        return ProductVariationDetailDto.builder()
                .variationValueFirstUuid(productVariationDetailEntity.getVariationValueFirst() != null ? productVariationDetailEntity.getVariationValueFirst().getProductVariationValueUuid() : null)
                .variationValueSecondUuid(productVariationDetailEntity.getVariationValueSecond() != null ? productVariationDetailEntity.getVariationValueSecond().getProductVariationValueUuid() : null)
                .price(productVariationDetailEntity.getPrice())
                .quantity(productVariationDetailEntity.getQuantity())
                .image(productVariationDetailEntity.getImage())
                .sku(productVariationDetailEntity.getSku())
                .build();
    }

    List<ProductVariationNameDto> toVariationNameDtos(List<ProductVariationNameEntity> productVariationNameEntities);

    List<ProductVariationValueDto> toVariationValueDtos(List<ProductVariationValueEntity> productVariationValueEntities);

    List<ProductVariationDetailDto> toVariationDetailDtos(List<ProductVariationDetailEntity> productVariationDetailEntities);

    @Mapping(source = "productEntity.productName", target = "productName")
    ProductVariationDetailIndexDto toVariationDetailIndexDto(ProductVariationDetailEntity productVariationDetailEntity);

    default List<ProductVariationDetailIndexDto> toVariationDetailIndexDtos(List<ProductVariationDetailEntity> productVariationDetailEntities) {
        return productVariationDetailEntities.stream()
                .map(this::toVariationDetailIndexDto)
                .collect(Collectors.toList());
    }
}


