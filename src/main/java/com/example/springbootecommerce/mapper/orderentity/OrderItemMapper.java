package com.example.springbootecommerce.mapper.orderentity;

import com.example.springbootecommerce.dto.orderentity.OrderItemEntityDto;
import com.example.springbootecommerce.model.OrderItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    @Mapping(source = "productEntity.productThumb", target = "image")
    @Mapping(source = "productVariationDetailEntity.sku", target = "sku")
    @Mapping(source = "productEntity.productName", target = "productName")
    @Mapping(source = "productEntity.id", target = "productId")
    @Mapping(source = "orderEntity.id", target = "orderId")
    @Mapping(source = "productVariationDetailEntity.id", target = "productVariationDetailId")
    OrderItemEntityDto toDto(OrderItemEntity orderItemEntity);
}
