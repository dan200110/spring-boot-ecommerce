package com.example.springbootecommerce.dto.productentity;

import com.example.springbootecommerce.dto.productcategoryentity.CategoryEntityCreateDto;
import com.example.springbootecommerce.dto.supplierentity.SupplierEntityCreateDto;
import com.example.springbootecommerce.dto.userentity.UserEntityIndexDto;

public class ProductEntityDetailDto {
    private int productId;
    private String productName;
    private String productDescription;
    private String productThumb;
    private String productType;
    private String productCode;
    private int numberSoled;
    private int totalQuantity;
    private CategoryEntityCreateDto categoryEntity;
    private SupplierEntityCreateDto supplierEntity;
    private UserEntityIndexDto shopEntity;
    private List<ProductVariationDto> variations;
}
