package com.example.springbootecommerce.mapper.userentity;

import com.example.springbootecommerce.dto.userentity.UserEntityIndexDto;
import com.example.springbootecommerce.model.RoleEntity;
import com.example.springbootecommerce.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)

public interface UserEntityIndexDtoMapper {
    UserEntity toEntity(UserEntityIndexDto userEntityIndexDto);

    UserEntityIndexDto toDto(UserEntity userEntity);

    @Mapping(target = "roles", expression = "java(mapRoles(userEntity))")
    UserEntityIndexDto toDtoWithCustomRoles(UserEntity userEntity);

    // Custom method to map roles from UserEntity to UserEntityIndexDto
    default Set<String> mapRoles(UserEntity userEntity) {
        Set<String> roleNames = new HashSet<>();
        if (userEntity != null && userEntity.getRoleEntities() != null) {
            roleNames = userEntity.getRoleEntities().stream()
                    .map(RoleEntity::getName)
                    .collect(Collectors.toSet());
        }
        return roleNames;
    }
}
