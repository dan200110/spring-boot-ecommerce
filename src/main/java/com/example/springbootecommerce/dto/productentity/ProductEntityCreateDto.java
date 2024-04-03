package com.example.springbootecommerce.dto.productentity;

import com.example.springbootecommerce.dto.productentity.productvariationentity.ProductVariationDetailDto;
import com.example.springbootecommerce.dto.productentity.productvariationentity.ProductVariationNameDto;
import com.example.springbootecommerce.dto.productentity.productvariationentity.ProductVariationValueDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductEntityCreateDto {
    private String productName;
    private String productThumb;
    private String productDescription;
    private String productType;
    private String productCode;
    private int categoryId;
    private int supplierId;
    private int userId;
    private List<ProductVariationNameDto> productVariationNames;
    private List<ProductVariationValueDto> productVariationValues;
    private List<ProductVariationDetailDto> productVariationDetails;
}
