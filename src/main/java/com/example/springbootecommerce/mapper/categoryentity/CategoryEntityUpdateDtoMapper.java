package com.example.springbootecommerce.mapper.categoryentity;


import com.example.springbootecommerce.dto.productcategoryentity.CategoryEntityUpdateDto;
import com.example.springbootecommerce.model.ProductCategoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryEntityUpdateDtoMapper {

    ProductCategoryEntity toEntity(CategoryEntityUpdateDto categoryEntityUpdateDto);

    CategoryEntityUpdateDto toDto(ProductCategoryEntity productCategoryEntity);
}
