package com.example.springbootecommerce.controller;

import com.example.springbootecommerce.dto.productentity.ProductEntityAfterCreatedDto;
import com.example.springbootecommerce.dto.productentity.ProductEntityCreateDto;
import com.example.springbootecommerce.dto.productentity.ProductEntityDetailDto;
import com.example.springbootecommerce.dto.productentity.ProductEntityIndexDto;
import com.example.springbootecommerce.repository.ProductEntityRepository;
import com.example.springbootecommerce.service.interfaces.ProductServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api/v1/products")
@RestController
@Slf4j
@RequiredArgsConstructor
public class ProductController {
    private final ProductServiceInterface productServiceInterface;
    private final ProductEntityRepository productEntityRepository;

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

    @GetMapping("/draft")
    public ResponseEntity<Page<ProductEntityIndexDto>> indexDraft(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ProductEntityIndexDto> productEntityIndexDtoPage =
                productServiceInterface.getAllDraftProducts(pageable);
        return ResponseEntity.ok(productEntityIndexDtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductEntityDetailDto> findById(@PathVariable("id") int id) {
        ProductEntityDetailDto productEntityDetailDto = productServiceInterface.getProductById(id);
        return ResponseEntity.ok(productEntityDetailDto);
    }

    @PutMapping("/{productId}/publish")
    public ResponseEntity<String> publishProduct(@PathVariable int productId) {
        return productServiceInterface.publishProduct(productId);
    }

    @PutMapping("/{productId}/unPublish")
    public ResponseEntity<String> unPublishProduct(@PathVariable int productId) {
        return productServiceInterface.unPublishProduct(productId);
    }

    @PostMapping("/{productId}/upload-image")
    public String uploadProductImages(@PathVariable int productId, @RequestParam("productImage") MultipartFile productImage) {
        productServiceInterface.uploadProductImage(productId, productImage);
        return "Product images uploaded successfully";
    }
}
