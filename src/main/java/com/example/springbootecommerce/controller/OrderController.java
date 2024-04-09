package com.example.springbootecommerce.controller;

import com.example.springbootecommerce.dto.orderentity.OrderEntityCreateDto;
import com.example.springbootecommerce.dto.orderentity.OrderEntityIndexDto;
import com.example.springbootecommerce.service.interfaces.OrderServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<Page<OrderEntityIndexDto>> getAllOrder(@PageableDefault(sort = "id", direction = Sort.Direction.ASC)Pageable pageable) {
        return ResponseEntity.ok(orderServiceInterface.getAllOrder(pageable));
    }
}
