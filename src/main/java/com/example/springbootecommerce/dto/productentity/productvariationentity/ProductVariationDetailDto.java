package com.example.springbootecommerce.dto.productentity.productvariationentity;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ProductVariationDetailDto {
    private UUID variationValueFirstUuid;
    private UUID variationValueSecondUuid;
    private int price;
    private Integer quantity;
    private String image;
}
