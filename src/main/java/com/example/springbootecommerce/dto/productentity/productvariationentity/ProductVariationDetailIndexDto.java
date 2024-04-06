package com.example.springbootecommerce.dto.productentity.productvariationentity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductVariationDetailIndexDto {
    private int id;
    private int price;
    private Integer quantity;
    private String image;
    private String sku;
    private String productName;
}
