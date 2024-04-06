package com.example.springbootecommerce.mapper;

import com.example.springbootecommerce.dto.cartentity.CartItemEntityIndexDto;
import com.example.springbootecommerce.model.CartItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    @Mapping(source = "productVariationDetailEntity", target = "productVariationDetailIndexDto")
    @Mapping(source = "productEntity", target = "productEntityAfterCreatedDto")
    CartItemEntityIndexDto cartItemEntityToDto(CartItemEntity cartItemEntity);
}
