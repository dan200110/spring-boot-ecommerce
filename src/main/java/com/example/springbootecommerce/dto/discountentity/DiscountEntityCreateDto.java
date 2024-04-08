package com.example.springbootecommerce.dto.discountentity;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
@Builder
public class DiscountEntityCreateDto {
    private String discountName;

    private String discountDescription;


    private String discountType;

    private int discountValue;

    private String discountCode;

    private Date discountStartDate;

    private Date discountEndDate;

    private int discountMaxUses;

    private int discountUsedCount;

    private int discountMaxUsePerUser;

    private int discountMinOrderValue;

    private boolean discountIsActive;

    private String discountAppliesTo;

    private int shopId;

    private Set<Integer> discountProducts;

}
