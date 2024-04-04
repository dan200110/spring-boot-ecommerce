package com.example.springbootecommerce.mapper.productentity.productvariation;

import com.example.springbootecommerce.dto.productentity.ProductEntityCreateDto;
import com.example.springbootecommerce.dto.productentity.productvariationentity.ProductVariationDetailDto;
import com.example.springbootecommerce.dto.productentity.productvariationentity.ProductVariationNameDto;
import com.example.springbootecommerce.dto.productentity.productvariationentity.ProductVariationValueDto;
import com.example.springbootecommerce.model.ProductEntity;
import com.example.springbootecommerce.model.ProductVariationDetailEntity;
import com.example.springbootecommerce.model.ProductVariationNameEntity;
import com.example.springbootecommerce.model.ProductVariationValueEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductVariationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productSlug", ignore = true)
    @Mapping(target = "numberSoled", ignore = true)
    @Mapping(target = "totalQuantity", ignore = true)
    @Mapping(target = "categoryEntity", ignore = true)
    @Mapping(target = "supplierEntity", ignore = true)
    @Mapping(target = "shopEntity", ignore = true)
    ProductEntity toProductEntity(ProductEntityCreateDto productEntityCreateDto);

    List<ProductVariationNameEntity> toVariationNameEntities(List<ProductVariationNameDto> productVariationNameDtos);

    List<ProductVariationValueEntity> toVariationValueEntities(List<ProductVariationValueDto> productVariationValueDtos);

    List<ProductVariationDetailEntity> toVariationDetailsEntities(List<ProductVariationDetailDto> productVariationDetailDtos);
}

