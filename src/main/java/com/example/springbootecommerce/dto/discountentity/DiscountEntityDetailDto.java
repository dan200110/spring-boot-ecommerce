package com.example.springbootecommerce.dto.discountentity;

import com.example.springbootecommerce.dto.productentity.ProductEntityAfterCreatedDto;
import com.example.springbootecommerce.dto.userentity.UserEntityIndexDto;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.Map;
import java.util.Set;

@Data
@Builder
public class DiscountEntityDetailDto {
    private int id;
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

    private UserEntityIndexDto shopEntity;

    private Set<ProductEntityAfterCreatedDto> discountProducts;
    private Map<UserEntityIndexDto, Integer> discountUsersUsed;
}
