package com.example.springbootecommerce.controller;

import com.example.springbootecommerce.dto.productentity.ProductEntityAfterCreatedDto;
import com.example.springbootecommerce.dto.productentity.ProductEntityCreateDto;
import com.example.springbootecommerce.dto.productentity.ProductEntityDetailDto;
import com.example.springbootecommerce.dto.productentity.ProductEntityIndexDto;
import com.example.springbootecommerce.repository.ProductEntityRepository;
import com.example.springbootecommerce.service.interfaces.ProductServiceInterface;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

@RequestMapping("/api/v1/products")
@RestController
@Slf4j
@RequiredArgsConstructor
public class ProductController {
    private final ProductServiceInterface productServiceInterface;
    private final RedisTemplate redisTemplate;
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

    private static final String REDIS_KEY_PREFIX = "product:";

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> findById(@PathVariable("id") int id) throws JsonProcessingException {
        String redisKey = REDIS_KEY_PREFIX + id;
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        // Attempt to retrieve the cached value from Redis
        String productJson = valueOperations.get(redisKey);

        if (productJson == null) {
            // Data not found in cache, retrieve from database
            ProductEntityDetailDto productEntityDetailDto = productServiceInterface.getProductById(id);

            // Convert ProductEntityDetailDto to JSON string
            ObjectMapper objectMapper = new ObjectMapper();
            productJson = objectMapper.writeValueAsString(productEntityDetailDto);

            // Cache the JSON string in Redis with setnx and expiration time
            valueOperations.setIfAbsent(redisKey, productJson, 1, TimeUnit.MINUTES);
        }

        // Return the JSON string with the appropriate content type
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(productJson);
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
