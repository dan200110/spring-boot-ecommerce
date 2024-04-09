package com.example.springbootecommerce.dto.cartentity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemEntitySimplyDto {
    private int id;
    private int quantity;
    private int price;
    private int sku;
    private int productName;
    private int productId;
    private int productVariationDetailId;
    private int userId;
}
