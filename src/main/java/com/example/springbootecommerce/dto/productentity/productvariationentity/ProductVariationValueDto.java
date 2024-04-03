package com.example.springbootecommerce.dto.productentity.productvariationentity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductVariationValueDto {
    private Long productVariationNameId;
    private String value;
}

