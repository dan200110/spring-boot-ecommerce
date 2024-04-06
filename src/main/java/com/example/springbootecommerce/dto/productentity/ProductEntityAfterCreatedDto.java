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

    private String productThumb;

    private String productDescription;

    private String productSlug;

    private String productType;

    private String productCode;

    private int numberSoled;

    private int totalQuantity;

    private int defaultPrice;

    private boolean isDraft;
}
