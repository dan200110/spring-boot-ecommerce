package com.example.springbootecommerce.mapper.supplierentity;

import com.example.springbootecommerce.dto.supplierentity.SupplierEntityCreateDto;
import com.example.springbootecommerce.model.SupplierEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SupplierEntityCreateDtoMapper {

    SupplierEntity toEntity(SupplierEntityCreateDto supplierEntityCreateDto);

    SupplierEntityCreateDto toDto(SupplierEntity supplierEntity);
}
