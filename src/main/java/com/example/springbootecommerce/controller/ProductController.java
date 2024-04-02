package com.example.springbootecommerce.controller;

import com.example.springbootecommerce.dto.productentity.ProductEntityAfterCreatedDto;
import com.example.springbootecommerce.dto.productentity.ProductEntityCreateDto;
import com.example.springbootecommerce.dto.productentity.ProductEntityIndexDto;
import com.example.springbootecommerce.service.interfaces.ProductServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/products")
@RestController
@Slf4j
@RequiredArgsConstructor
public class ProductController {
    private final ProductServiceInterface productServiceInterface;

    @PostMapping
    public ResponseEntity<ProductEntityAfterCreatedDto> create(
            @RequestBody ProductEntityCreateDto productEntityCreateDto) {
        return ResponseEntity.ok(productServiceInterface.createProduct(productEntityCreateDto));
    }

    @GetMapping
    public ResponseEntity<Page<ProductEntityIndexDto>> index(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ProductEntityIndexDto> productEntityIndexDtoPage =
                productServiceInterface.getAllProducts(pageable);
        return ResponseEntity.ok(productEntityIndexDtoPage);
    }
}
