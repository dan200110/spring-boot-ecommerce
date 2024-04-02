package com.example.springbootecommerce.mapper.productentity;

import com.example.springbootecommerce.dto.productentity.ProductEntityAfterCreatedDto;
import com.example.springbootecommerce.dto.productentity.ProductEntityCreateDto;
import com.example.springbootecommerce.dto.productentity.ProductEntityIndexDto;
import com.example.springbootecommerce.model.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productSlug", ignore = true)
    @Mapping(target = "categoryEntity.id", source = "categoryId")
    @Mapping(target = "supplierEntity.id", source = "supplierId")
    @Mapping(target = "shopEntity.id", source = "userId")
    ProductEntity toEntity(ProductEntityCreateDto dto);

    ProductEntityIndexDto toIndexDto(ProductEntity entity);

    ProductEntityAfterCreatedDto toAfterCreatedDto(ProductEntity entity);
}