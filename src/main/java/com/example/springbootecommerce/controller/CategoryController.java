package com.example.springbootecommerce.controller;

import com.example.springbootecommerce.dto.productcategoryentity.CategoryEntityCreateDto;
import com.example.springbootecommerce.dto.productcategoryentity.CategoryEntityUpdateDto;
import com.example.springbootecommerce.model.ProductCategoryEntity;
import com.example.springbootecommerce.service.interfaces.CategoryServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/categories")
@RestController
@Slf4j
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryServiceInterface categoryServiceInterface;

    @GetMapping
    public List<ProductCategoryEntity> getAllCategories() {
        return categoryServiceInterface.getAllCategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductCategoryEntity> getCategoryById(@PathVariable int id) {
        ProductCategoryEntity category = categoryServiceInterface.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @PostMapping
    public ResponseEntity<ProductCategoryEntity> createCategory(
            @RequestBody CategoryEntityCreateDto categoryEntityCreateDto) {
        ProductCategoryEntity createdCategory =
                categoryServiceInterface.createCategory(categoryEntityCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductCategoryEntity> updateCategory(
            @PathVariable int id, @RequestBody CategoryEntityUpdateDto categoryEntityUpdateDto) {
        categoryEntityUpdateDto.setId(id);
        ProductCategoryEntity productCategoryEntity =
                categoryServiceInterface.updateCategory(categoryEntityUpdateDto);

        return ResponseEntity.ok(productCategoryEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable int id) {
        categoryServiceInterface.deleteCategory(id);

        return ResponseEntity.ok().build();
    }
}