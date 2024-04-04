package com.example.springbootecommerce.dto.productentity;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ProductEntityAfterCreatedDto {
    private int id;
    private UUID productUuid;
    private String productName;
    private String productType;
    private String productThumb;
    private String productDescription;
    private Double numberSoled;
    private Integer totalQuantity;
}
