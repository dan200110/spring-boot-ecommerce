package com.example.springbootecommerce.controller;

import com.example.springbootecommerce.dto.cartentity.CartItemEntityCreateDto;
import com.example.springbootecommerce.dto.cartentity.CartItemEntityIndexDto;
import com.example.springbootecommerce.dto.cartentity.CartItemEntityUpdateDto;
import com.example.springbootecommerce.service.interfaces.CartItemServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/carts")
@RestController
@Slf4j
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemServiceInterface cartItemServiceInterface;

    @PostMapping
    public ResponseEntity<CartItemEntityIndexDto> addItemEntityToCart(@RequestBody CartItemEntityCreateDto cartItemEntityCreateDto) {
        return ResponseEntity.ok(
                cartItemServiceInterface.addItemEntityToCart(cartItemEntityCreateDto));
    }


    @GetMapping
    public ResponseEntity<List<CartItemEntityIndexDto>> getListCartItem() {
        return ResponseEntity.ok(
                cartItemServiceInterface.getCartItems());
    }

    @PutMapping
    public ResponseEntity<CartItemEntityIndexDto> updateCartItemQuantity(
            @RequestBody CartItemEntityUpdateDto cartItemEntityUpdateDto) {
        return ResponseEntity.ok(
                cartItemServiceInterface.updateCartItemQuantity(cartItemEntityUpdateDto));
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable("cartItemId") int cartItemId) {
        cartItemServiceInterface.deleteCartItem(cartItemId);
        return ResponseEntity.ok().build();
    }
}

