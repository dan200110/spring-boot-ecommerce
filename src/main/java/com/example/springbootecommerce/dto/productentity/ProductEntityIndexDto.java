package com.example.springbootecommerce.dto.productentity;

import com.example.springbootecommerce.dto.productcategoryentity.CategoryEntityCreateDto;
import com.example.springbootecommerce.dto.supplierentity.SupplierEntityCreateDto;
import com.example.springbootecommerce.dto.userentity.UserEntityIndexDto;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ProductEntityIndexDto {
    private int id;

    private UUID productUuid;

    private String productName;

    private String productThumb;

    private String productDescription;

    private String productSlug;

    private String productType;

    private String productCode;

    private int numberSoled;

    private int totalQuantity;

    private int defaultPrice;

    private CategoryEntityCreateDto categoryEntity;
    private SupplierEntityCreateDto supplierEntity;
    private UserEntityIndexDto shopEntity;
}
