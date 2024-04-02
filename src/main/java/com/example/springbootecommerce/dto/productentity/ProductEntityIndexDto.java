package com.example.springbootecommerce.dto.productentity;

import com.example.springbootecommerce.dto.productcategoryentity.CategoryEntityCreateDto;
import com.example.springbootecommerce.dto.supplierentity.SupplierEntityCreateDto;
import com.example.springbootecommerce.dto.userentity.UserEntityIndexDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductEntityIndexDto {
    private int id;

    private String productName;
    private String productType;
    private String productThumb;
    private String productDescription;
    private Double productPrice;
    private Integer productQuality;

    private CategoryEntityCreateDto categoryEntity;
    private SupplierEntityCreateDto supplierEntity;
    private UserEntityIndexDto shopEntity;
}
