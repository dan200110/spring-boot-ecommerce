package com.example.springbootecommerce.service.interfaces;

import com.example.springbootecommerce.dto.productcategoryentity.CategoryEntityCreateDto;
import com.example.springbootecommerce.dto.productcategoryentity.CategoryEntityUpdateDto;
import com.example.springbootecommerce.model.ProductCategoryEntity;

import java.util.List;

public interface CategoryServiceInterface {
    List<ProductCategoryEntity> getAllCategories();

    ProductCategoryEntity getCategoryById(int id);

    ProductCategoryEntity createCategory(CategoryEntityCreateDto categoryEntityCreateDto);

    ProductCategoryEntity updateCategory(CategoryEntityUpdateDto categoryEntityUpdateDto);

    void deleteCategory(int id);
}
