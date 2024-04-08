package com.example.springbootecommerce.controller;

import com.example.springbootecommerce.dto.orderentity.OrderEntityCreateDto;
import com.example.springbootecommerce.dto.orderentity.OrderEntityIndexDto;
import com.example.springbootecommerce.service.interfaces.OrderServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/orders")
@RestController
@Slf4j
@RequiredArgsConstructor
public class OrderController {
    private final OrderServiceInterface orderServiceInterface;

    @PostMapping
    public ResponseEntity<OrderEntityIndexDto> createOrder(@RequestBody OrderEntityCreateDto orderEntityCreateDto) {
        return ResponseEntity.ok(orderServiceInterface.createOrder(orderEntityCreateDto));
    }

}
