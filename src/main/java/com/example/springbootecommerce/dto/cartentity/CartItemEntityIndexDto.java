package com.example.springbootecommerce.dto.cartentity;

import com.example.springbootecommerce.dto.productentity.ProductEntityAfterCreatedDto;
import com.example.springbootecommerce.dto.productentity.productvariationentity.ProductVariationDetailIndexDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemEntityIndexDto {

    private int id;

    private Integer quantity;

    private ProductVariationDetailIndexDto productVariationDetailIndexDto;
    private ProductEntityAfterCreatedDto productEntityAfterCreatedDto;
}