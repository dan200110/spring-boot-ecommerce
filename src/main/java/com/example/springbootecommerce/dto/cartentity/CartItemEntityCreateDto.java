package com.example.springbootecommerce.dto.cartentity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemEntityCreateDto {
    private int quantity;
    private int productVariationDetailEntityId;
}
