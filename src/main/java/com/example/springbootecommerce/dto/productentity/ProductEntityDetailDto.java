package com.example.springbootecommerce.dto.productentity;

import com.example.springbootecommerce.dto.productcategoryentity.CategoryEntityCreateDto;
import com.example.springbootecommerce.dto.productentity.productvariationentity.ProductVariationDetailDto;
import com.example.springbootecommerce.dto.productentity.productvariationentity.ProductVariationNameDto;
import com.example.springbootecommerce.dto.productentity.productvariationentity.ProductVariationValueDto;
import com.example.springbootecommerce.dto.supplierentity.SupplierEntityCreateDto;
import com.example.springbootecommerce.dto.userentity.UserEntityIndexDto;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ProductEntityDetailDto {
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
    private boolean isDraft;

    private CategoryEntityCreateDto categoryEntity;
    private SupplierEntityCreateDto supplierEntity;
    private UserEntityIndexDto shopEntity;
    private List<ProductVariationNameDto> productVariationNames;
    private List<ProductVariationValueDto> productVariationValues;
    private List<ProductVariationDetailDto> productVariationDetails;
}
