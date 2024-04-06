package com.example.springbootecommerce.controller;

import com.example.springbootecommerce.dto.productentity.productvariationentity.ProductVariationDetailIndexDto;
import com.example.springbootecommerce.service.interfaces.ProductDetailServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/api/v1/product_details")
@RestController
@Slf4j
@RequiredArgsConstructor
public class ProductDetailController {
    private final ProductDetailServiceInterface productDetailServiceInterface;

    @PostMapping("/{productDetailId}/upload-image")
    public String uploadProductImages(@PathVariable int productDetailId, @RequestParam("productDetailImage") MultipartFile productDetailImage) {
        productDetailServiceInterface.uploadProductDetailImage(productDetailId, productDetailImage);
        return "Product images uploaded successfully";
    }

    @GetMapping("/getAllDetailByProductId")
    public ResponseEntity<List<ProductVariationDetailIndexDto>> index(
            @RequestParam int productId) {
        List<ProductVariationDetailIndexDto> productVariationDetailDtos =
                productDetailServiceInterface.getAllProductDetailByProductId(productId);
        return ResponseEntity.ok(productVariationDetailDtos);
    }
}
