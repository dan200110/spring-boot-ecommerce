package com.example.springbootecommerce.dto.orderentity;

import com.example.springbootecommerce.constant.OrderStatus;
import com.example.springbootecommerce.dto.userentity.UserEntityIndexDto;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class OrderEntityIndexDto {
    private int id;

    private String orderCode;

    private OrderStatus orderStatus;

    private int totalPrice;

    private int feeShip;

    private int totalDiscount;

    private int totalCheckout;

    private String address;

    private String phoneNumber;

    private UserEntityIndexDto userEntity;

    private Set<Integer> cartItemIds;
    private Set<Integer> discountIds;
}
