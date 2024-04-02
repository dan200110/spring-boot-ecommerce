package com.example.springbootecommerce.mapper.categoryentity;


import com.example.springbootecommerce.dto.productcategoryentity.CategoryEntityCreateDto;
import com.example.springbootecommerce.model.ProductCategoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryEntityCreateDtoMapper {

    ProductCategoryEntity toEntity(CategoryEntityCreateDto categoryEntityCreateDto);

    CategoryEntityCreateDto toDto(ProductCategoryEntity productCategoryEntity);
}
