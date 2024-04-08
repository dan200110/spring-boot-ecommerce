package com.example.springbootecommerce.service.interfaces;

import com.example.springbootecommerce.dto.orderentity.OrderEntityCreateDto;
import com.example.springbootecommerce.dto.orderentity.OrderEntityIndexDto;

public interface OrderServiceInterface {
    OrderEntityIndexDto createOrder(OrderEntityCreateDto orderEntityCreateDto);
}
