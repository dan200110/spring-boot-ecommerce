package com.example.springbootecommerce.dto.productentity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductEntityAfterCreatedDto {
    private int id;
    private String productName;
    private String productType;
    private String productThumb;
    private String productDescription;
    private Double productPrice;
    private Integer productQuality;
}
