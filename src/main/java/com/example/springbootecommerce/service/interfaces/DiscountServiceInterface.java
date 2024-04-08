package com.example.springbootecommerce.service.interfaces;

import com.example.springbootecommerce.dto.discountentity.DiscountEntityCreateDto;
import com.example.springbootecommerce.dto.discountentity.DiscountEntityDetailDto;
import com.example.springbootecommerce.dto.discountentity.DiscountEntityIndexDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DiscountServiceInterface {
    DiscountEntityIndexDto createDiscount(DiscountEntityCreateDto discountEntityCreateDto);

    Page<DiscountEntityIndexDto> listDiscountOfCurrentShop(Pageable pageable);

    DiscountEntityDetailDto getDiscountDetail(int id);
}
