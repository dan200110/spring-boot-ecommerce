package com.example.springbootecommerce.mapper.userentity;

import com.example.springbootecommerce.dto.userentity.UserEntityLoggedInDto;
import com.example.springbootecommerce.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface UserEntityLoggedInDtoMapper {
    UserEntity toEntity(UserEntityLoggedInDto userEntityLoggedInDto);

    UserEntityLoggedInDto toDto(UserEntity userEntity);
}
