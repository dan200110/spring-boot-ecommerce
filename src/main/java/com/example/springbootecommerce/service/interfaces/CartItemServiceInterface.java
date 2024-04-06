package com.example.springbootecommerce.service.interfaces;

import com.example.springbootecommerce.dto.cartentity.CartItemEntityCreateDto;
import com.example.springbootecommerce.dto.cartentity.CartItemEntityIndexDto;
import com.example.springbootecommerce.dto.cartentity.CartItemEntityUpdateDto;

import java.util.List;

public interface CartItemServiceInterface {
    CartItemEntityIndexDto addItemEntityToCart(CartItemEntityCreateDto cartItemEntityCreateDto);

    List<CartItemEntityIndexDto> getCartItems();

    CartItemEntityIndexDto updateCartItemQuantity(CartItemEntityUpdateDto cartItemEntityUpdateDto);

    void deleteCartItem(int cartItemId);
}
