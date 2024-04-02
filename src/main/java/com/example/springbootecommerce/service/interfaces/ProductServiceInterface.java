package com.example.springbootecommerce.service.interfaces;

import com.example.springbootecommerce.dto.productentity.ProductEntityAfterCreatedDto;
import com.example.springbootecommerce.dto.productentity.ProductEntityCreateDto;
import com.example.springbootecommerce.dto.productentity.ProductEntityIndexDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductServiceInterface {

    Page<ProductEntityIndexDto> getAllProducts(Pageable pageable);

    ProductEntityAfterCreatedDto createProduct(ProductEntityCreateDto productEntityCreateDto);

}
