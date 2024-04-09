package com.example.springbootecommerce.mapper.cartentity;

import com.example.springbootecommerce.dto.cartentity.CartItemEntityIndexDto;
import com.example.springbootecommerce.dto.cartentity.CartItemEntitySimplyDto;
import com.example.springbootecommerce.model.CartItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    @Mapping(source = "productVariationDetailEntity", target = "productVariationDetailIndexDto")
    @Mapping(source = "productEntity", target = "productEntityAfterCreatedDto")
    CartItemEntityIndexDto cartItemEntityToDto(CartItemEntity cartItemEntity);

    @Mapping(source = "productVariationDetailEntity.price", target = "price")
    @Mapping(source = "productVariationDetailEntity.sku", target = "sku")
    @Mapping(source = "productEntity.productName", target = "productName")
    @Mapping(source = "productEntity.id", target = "productId")
    @Mapping(source = "productVariationDetailEntity.id", target = "productVariationDetailId")
    @Mapping(source = "userEntity.id", target = "userId")
    CartItemEntitySimplyDto cartItemEntityToSimplyDto(CartItemEntity cartItemEntity);
}
