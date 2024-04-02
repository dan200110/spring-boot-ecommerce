package com.example.springbootecommerce.service.implementations;

import com.example.springbootecommerce.dto.supplierentity.SupplierEntityCreateDto;
import com.example.springbootecommerce.dto.supplierentity.SupplierEntityUpdateDto;
import com.example.springbootecommerce.exception.ResourceDuplicateException;
import com.example.springbootecommerce.exception.ResourceNotFoundException;
import com.example.springbootecommerce.mapper.supplierentity.SupplierEntityCreateDtoMapper;
import com.example.springbootecommerce.mapper.supplierentity.SupplierEntityUpdateDtoMapper;
import com.example.springbootecommerce.model.SupplierEntity;
import com.example.springbootecommerce.repository.SupplierEntityRepository;
import com.example.springbootecommerce.service.interfaces.SupplierServiceInterface;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Getter
@Setter
@RequiredArgsConstructor
@Transactional
public class SupplierServiceImpl implements SupplierServiceInterface {

    private final SupplierEntityRepository supplierEntityRepository;

    private final SupplierEntityCreateDtoMapper supplierEntityCreateDtoMapper;

    private final SupplierEntityUpdateDtoMapper supplierEntityUpdateDtoMapper;

    public List<SupplierEntity> getAllSuppliers() {
        return supplierEntityRepository.findAll();
    }

    public SupplierEntity getSupplierById(int id) {
        Optional<SupplierEntity> supplierEntity = supplierEntityRepository.findById(id);
        if (supplierEntity.isPresent()) {
            return supplierEntity.get();
        } else {
            throw new ResourceNotFoundException("Could not find supplier with id " + id);
        }
    }

    public SupplierEntity createSupplier(SupplierEntityCreateDto supplierEntityCreateDto) {
        if (Boolean.TRUE.equals(
                supplierEntityRepository.existsByName(supplierEntityCreateDto.getName()))) {
            throw new ResourceDuplicateException("Supplier with this name already exists");
        }

        SupplierEntity supplierEntity = supplierEntityCreateDtoMapper.toEntity(supplierEntityCreateDto);
        return supplierEntityRepository.save(supplierEntity);
    }

    public SupplierEntity updateSupplier(SupplierEntityUpdateDto supplierEntityUpdateDto) {
        if (supplierEntityRepository.existsById(supplierEntityUpdateDto.getId())) {
            SupplierEntity supplierEntity =
                    supplierEntityUpdateDtoMapper.toEntity(supplierEntityUpdateDto);
            return supplierEntityRepository.save(supplierEntity);
        } else {
            throw new ResourceNotFoundException(
                    "Could not find supplier with id " + supplierEntityUpdateDto.getId());
        }
    }

    public void deleteSupplier(int id) {
        if (supplierEntityRepository.existsById(id)) {
            supplierEntityRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Could not find supplier with id " + id);
        }
    }
}
