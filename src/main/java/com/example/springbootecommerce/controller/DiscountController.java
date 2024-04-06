package com.example.springbootecommerce.controller;

import com.example.springbootecommerce.dto.discountentity.DiscountEntityCreateDto;
import com.example.springbootecommerce.dto.discountentity.DiscountEntityIndexDto;
import com.example.springbootecommerce.service.interfaces.DiscountServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/discounts")
@RestController
@Slf4j
@RequiredArgsConstructor
public class DiscountController {
    private final DiscountServiceInterface discountServiceInterface;

    @PostMapping
    public ResponseEntity<DiscountEntityIndexDto> createDiscount(
            @RequestBody DiscountEntityCreateDto discountEntityCreateDto) {

        return ResponseEntity.ok(discountServiceInterface.createDiscount(discountEntityCreateDto));
    }
}
