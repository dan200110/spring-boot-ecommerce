package com.example.springbootecommerce.dto.productentity.productvariationentity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductVariationDetailDto {
    private int variationValueFirstId;
    private int variationValueSecondId;
    private Double price;
    private Integer quantity;
    private String image;
}
