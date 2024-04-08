package com.example.springbootecommerce.mapper.discountentity;

import com.example.springbootecommerce.dto.discountentity.DiscountEntityCreateDto;
import com.example.springbootecommerce.dto.discountentity.DiscountEntityDetailDto;
import com.example.springbootecommerce.dto.discountentity.DiscountEntityIndexDto;
import com.example.springbootecommerce.model.DiscountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface DiscountMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "discountCode", source = "discountCode")
        // You may need to customize this based on your entity structure
    DiscountEntity toEntity(DiscountEntityCreateDto dto);

    DiscountEntityIndexDto toIndexDto(DiscountEntity entity);

    @Mapping(target = "discountProducts", ignore = true)
    @Mapping(target = "discountUsersUsed", ignore = true)
    DiscountEntityDetailDto toDetailDto(DiscountEntity discountEntity);
}