package com.example.springbootecommerce.service.interfaces;

import com.example.springbootecommerce.dto.productentity.ProductEntityAfterCreatedDto;
import com.example.springbootecommerce.dto.productentity.ProductEntityCreateDto;
import com.example.springbootecommerce.dto.productentity.ProductEntityDetailDto;
import com.example.springbootecommerce.dto.productentity.ProductEntityIndexDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ProductServiceInterface {

    Page<ProductEntityIndexDto> getAllProducts(Pageable pageable);

    ProductEntityAfterCreatedDto createProduct(ProductEntityCreateDto productEntityCreateDto);

    ProductEntityDetailDto getProductById(int id);

    Page<ProductEntityIndexDto> getAllDraftProducts(Pageable pageable);

    void uploadProductImage(int productId, MultipartFile productImage);

    ResponseEntity<String> publishProduct(int productId);

    ResponseEntity<String> unPublishProduct(int productId);
}
