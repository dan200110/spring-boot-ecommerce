package com.example.springbootecommerce.service.implementations;

import com.example.springbootecommerce.dto.productcategoryentity.CategoryEntityCreateDto;
import com.example.springbootecommerce.dto.productcategoryentity.CategoryEntityUpdateDto;
import com.example.springbootecommerce.exception.ResourceDuplicateException;
import com.example.springbootecommerce.exception.ResourceNotFoundException;
import com.example.springbootecommerce.mapper.categoryentity.CategoryEntityCreateDtoMapper;
import com.example.springbootecommerce.mapper.categoryentity.CategoryEntityUpdateDtoMapper;
import com.example.springbootecommerce.model.ProductCategoryEntity;
import com.example.springbootecommerce.repository.ProductCategoryEntityRepository;
import com.example.springbootecommerce.service.interfaces.CategoryServiceInterface;
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
public class CategoryServiceImpl implements CategoryServiceInterface {

    private final ProductCategoryEntityRepository productCategoryEntityRepository;

    private final CategoryEntityCreateDtoMapper categoryEntityCreateDtoMapper;

    private final CategoryEntityUpdateDtoMapper categoryEntityUpdateDtoMapper;

    public List<ProductCategoryEntity> getAllCategories() {
        return productCategoryEntityRepository.findAll();
    }

    public ProductCategoryEntity getCategoryById(int id) {
        Optional<ProductCategoryEntity> productCategoryEntity =
                productCategoryEntityRepository.findById(id);
        if (productCategoryEntity.isPresent()) {
            return productCategoryEntity.get();
        } else {
            throw new ResourceNotFoundException("Could not find category with id " + id);
        }
    }

    public ProductCategoryEntity createCategory(CategoryEntityCreateDto categoryEntityCreateDto) {
        if (productCategoryEntityRepository.existsByName(categoryEntityCreateDto.getName())) {
            throw new ResourceDuplicateException("this category is already exist");
        } else {
            ProductCategoryEntity productCategoryEntity =
                    categoryEntityCreateDtoMapper.toEntity(categoryEntityCreateDto);
            return productCategoryEntityRepository.save(productCategoryEntity);
        }
    }

    public ProductCategoryEntity updateCategory(CategoryEntityUpdateDto categoryEntityUpdateDto) {
        if (productCategoryEntityRepository.existsById(categoryEntityUpdateDto.getId())) {
            ProductCategoryEntity productCategoryEntity =
                    categoryEntityUpdateDtoMapper.toEntity(categoryEntityUpdateDto);
            return productCategoryEntityRepository.save(productCategoryEntity);
        } else {
            throw new ResourceNotFoundException(
                    "Could not find category with id " + categoryEntityUpdateDto.getId());
        }
    }

    public void deleteCategory(int id) {
        if (productCategoryEntityRepository.existsById(id)) {
            productCategoryEntityRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Could not find category with id " + id);
        }
    }
}
