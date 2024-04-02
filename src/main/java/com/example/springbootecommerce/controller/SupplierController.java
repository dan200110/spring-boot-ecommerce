package com.example.springbootecommerce.controller;

import com.example.springbootecommerce.dto.supplierentity.SupplierEntityCreateDto;
import com.example.springbootecommerce.dto.supplierentity.SupplierEntityUpdateDto;
import com.example.springbootecommerce.model.SupplierEntity;
import com.example.springbootecommerce.service.interfaces.SupplierServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/suppliers")
@RestController
@Slf4j
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierServiceInterface supplierServiceInterface;

    @GetMapping
    public List<SupplierEntity> getAllSuppliers() {
        return supplierServiceInterface.getAllSuppliers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierEntity> getSupplierById(@PathVariable int id) {
        SupplierEntity supplier = supplierServiceInterface.getSupplierById(id);
        return ResponseEntity.ok(supplier);
    }

    @PostMapping
    public ResponseEntity<SupplierEntity> createSupplier(
            @RequestBody SupplierEntityCreateDto supplierEntityCreateDto) {
        SupplierEntity createdSupplier =
                supplierServiceInterface.createSupplier(supplierEntityCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSupplier);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierEntity> updateSupplier(
            @PathVariable int id, @RequestBody SupplierEntityUpdateDto supplierEntityUpdateDto) {
        supplierEntityUpdateDto.setId(id);
        SupplierEntity updatedSupplier =
                supplierServiceInterface.updateSupplier(supplierEntityUpdateDto);
        return ResponseEntity.ok(updatedSupplier);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable int id) {
        supplierServiceInterface.deleteSupplier(id);
        return ResponseEntity.ok().build();
    }
}