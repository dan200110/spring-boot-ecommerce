package com.example.springbootecommerce.service.interfaces;

import com.example.springbootecommerce.dto.productentity.productvariationentity.ProductVariationDetailIndexDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductDetailServiceInterface {
    void uploadProductDetailImage(int productDetailId, MultipartFile productDetailImage);

    List<ProductVariationDetailIndexDto> getAllProductDetailByProductId(int productId);
}
