package com.example.springbootecommerce.dto.discountentity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DiscountUsageSimplyDto {
    private int userId;
    private int usageCount;
}
