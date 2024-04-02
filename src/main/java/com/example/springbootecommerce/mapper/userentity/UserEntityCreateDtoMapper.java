package com.example.springbootecommerce.mapper.userentity;

import com.example.springbootecommerce.dto.userentity.UserEntityCreateDto;
import com.example.springbootecommerce.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface UserEntityCreateDtoMapper {
    UserEntity toEntity(UserEntityCreateDto userEntityCreateDto);
    UserEntityCreateDto toDto(UserEntity userEntity);
}
