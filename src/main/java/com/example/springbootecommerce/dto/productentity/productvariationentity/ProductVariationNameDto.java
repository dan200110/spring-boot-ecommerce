package com.example.springbootecommerce.dto.productentity.productvariationentity;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ProductVariationNameDto {
    private UUID productVariationNameUuid;
    private String name;
    private String description;
}
