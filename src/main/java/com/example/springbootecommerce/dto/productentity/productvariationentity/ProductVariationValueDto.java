package com.example.springbootecommerce.dto.productentity.productvariationentity;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ProductVariationValueDto {
    private UUID productVariationValueUuid;
    private UUID productVariationNameUuid;
    private String value;
}

