package com.example.springbootecommerce.service.interfaces;

import com.example.springbootecommerce.dto.orderentity.OrderEntityCreateDto;
import com.example.springbootecommerce.dto.orderentity.OrderEntityIndexDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderServiceInterface {
    OrderEntityIndexDto createOrder(OrderEntityCreateDto orderEntityCreateDto);

    Page<OrderEntityIndexDto> getAllOrder(Pageable pageable);
}
