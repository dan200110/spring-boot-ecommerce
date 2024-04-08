package com.example.springbootecommerce.controller;

import com.example.springbootecommerce.dto.discountentity.DiscountEntityCreateDto;
import com.example.springbootecommerce.dto.discountentity.DiscountEntityDetailDto;
import com.example.springbootecommerce.dto.discountentity.DiscountEntityIndexDto;
import com.example.springbootecommerce.service.interfaces.DiscountServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<Page<DiscountEntityIndexDto>> listDiscountByCurrentShop(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(discountServiceInterface.listDiscountOfCurrentShop(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiscountEntityDetailDto> findById(@PathVariable("id") int id) {
        DiscountEntityDetailDto discountEntityDetailDto = discountServiceInterface.getDiscountDetail(id);
        return ResponseEntity.ok(discountEntityDetailDto);
    }
}
