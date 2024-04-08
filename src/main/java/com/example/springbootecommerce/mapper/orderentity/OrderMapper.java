package com.example.springbootecommerce.mapper.orderentity;

import com.example.springbootecommerce.dto.orderentity.OrderEntityCreateDto;
import com.example.springbootecommerce.dto.orderentity.OrderEntityIndexDto;
import com.example.springbootecommerce.model.OrderEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderEntity orderEntityCreateDtoToEntity(OrderEntityCreateDto orderEntityCreateDto);

    OrderEntityIndexDto orderEntityToIndexDto(OrderEntity orderEntity);
}
