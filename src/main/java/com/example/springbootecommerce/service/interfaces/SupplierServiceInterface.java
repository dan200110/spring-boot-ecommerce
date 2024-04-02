package com.example.springbootecommerce.service.interfaces;

import com.example.springbootecommerce.dto.supplierentity.SupplierEntityCreateDto;
import com.example.springbootecommerce.dto.supplierentity.SupplierEntityUpdateDto;
import com.example.springbootecommerce.model.SupplierEntity;

import java.util.List;

public interface SupplierServiceInterface {

    List<SupplierEntity> getAllSuppliers();

    SupplierEntity getSupplierById(int id);

    SupplierEntity createSupplier(SupplierEntityCreateDto supplierEntityCreateDto);

    SupplierEntity updateSupplier(SupplierEntityUpdateDto supplierEntityUpdateDto);

    void deleteSupplier(int id);
}