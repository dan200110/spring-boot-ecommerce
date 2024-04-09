package com.example.springbootecommerce.dto.orderentity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemEntityDto {
    private int id;
    private int quantity;
    private int price;
    private String sku;
    private String image;
    private String productName;
    private int productId;
    private int productVariationDetailId;
    private int orderId;
}
