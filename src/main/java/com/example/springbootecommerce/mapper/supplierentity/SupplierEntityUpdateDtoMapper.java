package com.example.springbootecommerce.mapper.supplierentity;

import com.example.springbootecommerce.dto.supplierentity.SupplierEntityUpdateDto;
import com.example.springbootecommerce.model.SupplierEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SupplierEntityUpdateDtoMapper {

    SupplierEntity toEntity(SupplierEntityUpdateDto supplierEntityUpdateDto);

    SupplierEntityUpdateDto toDto(SupplierEntity supplierEntity);
}
