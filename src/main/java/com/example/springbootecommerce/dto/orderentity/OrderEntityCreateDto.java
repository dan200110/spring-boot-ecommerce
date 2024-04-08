package com.example.springbootecommerce.dto.orderentity;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class OrderEntityCreateDto {
    private String orderCode;
    private String address;
    private String phoneNumber;
    private Set<Integer> cartItemIds;
    private Set<Integer> discountIds;
}
