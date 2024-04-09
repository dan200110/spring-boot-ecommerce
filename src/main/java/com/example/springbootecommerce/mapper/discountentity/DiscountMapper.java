package com.example.springbootecommerce.mapper.discountentity;

import com.example.springbootecommerce.dto.discountentity.DiscountEntityCreateDto;
import com.example.springbootecommerce.dto.discountentity.DiscountEntityDetailDto;
import com.example.springbootecommerce.dto.discountentity.DiscountEntityIndexDto;
import com.example.springbootecommerce.dto.discountentity.DiscountUsageSimplyDto;
import com.example.springbootecommerce.model.DiscountEntity;
import com.example.springbootecommerce.model.DiscountUsageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface DiscountMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "discountCode", source = "discountCode")
        // You may need to customize this based on your entity structure
    DiscountEntity toEntity(DiscountEntityCreateDto dto);

    @Mapping(target = "discountProducts", ignore = true)
    @Mapping(target = "discountUsersUsed", ignore = true)
    DiscountEntityDetailDto toDetailDto(DiscountEntity discountEntity);


    @Mapping(target = "discountUsage", source = "discountUsageEntities")
    DiscountEntityIndexDto toIndexDto(DiscountEntity discountEntity);

    default Set<DiscountUsageSimplyDto> mapDiscountUsageToDto(Set<DiscountUsageEntity> discountUsageEntities) {
        Set<DiscountUsageSimplyDto> result = new HashSet<>();
        for (DiscountUsageEntity usage : discountUsageEntities) {
            DiscountUsageSimplyDto dto = DiscountUsageSimplyDto.builder()
                    .userId(usage.getUserEntity().getId())
                    .usageCount(usage.getUsageCount())
                    .build();
            result.add(dto);
        }
        return result;
    }
}