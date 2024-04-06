package com.example.springbootecommerce.service.implementations;

import com.example.springbootecommerce.dto.discountentity.DiscountEntityCreateDto;
import com.example.springbootecommerce.dto.discountentity.DiscountEntityIndexDto;
import com.example.springbootecommerce.repository.DiscountEntityRepository;
import com.example.springbootecommerce.service.interfaces.DiscountServiceInterface;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Getter
@Setter
@RequiredArgsConstructor
@Transactional
public class DiscountServiceImpl implements DiscountServiceInterface {
    private final DiscountEntityRepository discountEntityRepository;

    @Override
    public DiscountEntityIndexDto createDiscount(DiscountEntityCreateDto discountEntityCreateDto) {
        return null;
    }
}
