package com.example.springbootecommerce.mapper.orderentity;

import com.example.springbootecommerce.dto.discountentity.DiscountEntityIndexDto;
import com.example.springbootecommerce.dto.orderentity.OrderEntityCreateDto;
import com.example.springbootecommerce.dto.orderentity.OrderEntityIndexDto;
import com.example.springbootecommerce.dto.orderentity.OrderItemEntityDto;
import com.example.springbootecommerce.mapper.discountentity.DiscountMapper;
import com.example.springbootecommerce.model.DiscountEntity;
import com.example.springbootecommerce.model.OrderEntity;
import com.example.springbootecommerce.model.OrderItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderEntity orderEntityCreateDtoToEntity(OrderEntityCreateDto orderEntityCreateDto);

    OrderEntityIndexDto orderEntityToIndexDto(OrderEntity orderEntity);

    default Set<OrderItemEntityDto> mapOrderItemsToDtos(Set<OrderItemEntity> orderItemEntities) {
        return orderItemEntities.stream()
                .map(this::mapOrderItemEntityToDto)
                .collect(Collectors.toSet());
    }

    default OrderItemEntityDto mapOrderItemEntityToDto(OrderItemEntity orderItemEntity) {
        OrderItemMapper orderItemMapper = Mappers.getMapper(OrderItemMapper.class);
        return orderItemMapper.toDto(orderItemEntity);
    }

    default Set<DiscountEntityIndexDto> mapDiscountsToDtos(Set<DiscountEntity> discountEntities) {
        return discountEntities.stream()
                .map(this::mapDiscountEntityToDto)
                .collect(Collectors.toSet());
    }

    default DiscountEntityIndexDto mapDiscountEntityToDto(DiscountEntity discountEntity) {
        DiscountMapper discountMapper = Mappers.getMapper(DiscountMapper.class);
        return discountMapper.toIndexDto(discountEntity);
    }
}
