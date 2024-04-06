package com.example.springbootecommerce.service.interfaces;

import com.example.springbootecommerce.dto.discountentity.DiscountEntityCreateDto;
import com.example.springbootecommerce.dto.discountentity.DiscountEntityIndexDto;

public interface DiscountServiceInterface {
    DiscountEntityIndexDto createDiscount(DiscountEntityCreateDto discountEntityCreateDto);
}
