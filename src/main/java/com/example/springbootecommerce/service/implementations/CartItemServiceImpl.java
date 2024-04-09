package com.example.springbootecommerce.service.implementations;

import com.example.springbootecommerce.dto.cartentity.CartItemEntityCreateDto;
import com.example.springbootecommerce.dto.cartentity.CartItemEntityIndexDto;
import com.example.springbootecommerce.dto.cartentity.CartItemEntityUpdateDto;
import com.example.springbootecommerce.exception.InvalidStateException;
import com.example.springbootecommerce.exception.ResourceNotFoundException;
import com.example.springbootecommerce.mapper.cartentity.CartItemMapper;
import com.example.springbootecommerce.model.CartItemEntity;
import com.example.springbootecommerce.model.ProductEntity;
import com.example.springbootecommerce.model.ProductVariationDetailEntity;
import com.example.springbootecommerce.model.UserEntity;
import com.example.springbootecommerce.repository.CartItemEntityRepository;
import com.example.springbootecommerce.repository.ProductEntityRepository;
import com.example.springbootecommerce.repository.ProductVariationDetailRepository;
import com.example.springbootecommerce.service.interfaces.CartItemServiceInterface;
import com.example.springbootecommerce.service.interfaces.UserServiceInterface;
import com.example.springbootecommerce.util.AuthencationUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Getter
@Setter
@RequiredArgsConstructor
@Transactional
public class CartItemServiceImpl implements CartItemServiceInterface {
    private final UserServiceInterface userServiceInterface;
    private final ProductVariationDetailRepository productVariationDetailRepository;
    private final ProductEntityRepository productEntityRepository;
    private final CartItemEntityRepository cartItemEntityRepository;
    private final CartItemMapper cartItemMapper;
    private final AuthencationUtils authencationUtils;

    @Override
    public CartItemEntityIndexDto addItemEntityToCart(CartItemEntityCreateDto cartItemEntityCreateDto) {

        Optional<ProductVariationDetailEntity> productVariationDetailEntityOptional = productVariationDetailRepository.findById(cartItemEntityCreateDto.getProductVariationDetailEntityId());
        ProductVariationDetailEntity productVariationDetailEntity = productVariationDetailEntityOptional.orElseThrow(() -> new ResourceNotFoundException("Could not find productVariationDetail entity"));

        Optional<ProductEntity> productEntityOptional = productEntityRepository.findById(productVariationDetailEntity.getProductEntity().getId());
        ProductEntity productEntity = productEntityOptional.orElseThrow(() -> new ResourceNotFoundException("Could not find product entity"));

        UserEntity userEntity = authencationUtils.extractUserFromAuthentication();

        if (productVariationDetailEntity.getSku() == null || productVariationDetailEntity.getPrice() < 0 || productEntity.isDraft()) {
            throw new InvalidStateException("The requested item is wrong");
        } else if (productVariationDetailEntity.getQuantity() < cartItemEntityCreateDto.getQuantity()) {
            throw new InvalidStateException(
                    "The requested item stock is smaller than the requested quantity");
        }

        CartItemEntity newCartItemEntity =
                CartItemEntity.builder()
                        .userEntity(userEntity)
                        .productEntity(productEntity)
                        .productVariationDetailEntity(productVariationDetailEntity)
                        .quantity(cartItemEntityCreateDto.getQuantity())
                        .build();

        cartItemEntityRepository.save(newCartItemEntity);
        return cartItemMapper.cartItemEntityToDto(newCartItemEntity);

    }

    @Override
    public List<CartItemEntityIndexDto> getCartItems() {
        UserEntity userEntity = authencationUtils.extractUserFromAuthentication();

        List<CartItemEntity> cartItemEntities = cartItemEntityRepository.findByUserEntityId(userEntity.getId());

        return cartItemEntities.stream()
                .map(cartItemMapper::cartItemEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CartItemEntityIndexDto updateCartItemQuantity(CartItemEntityUpdateDto cartItemEntityUpdateDto) {
        checkCorrectUser(cartItemEntityUpdateDto.getCartItemId());
        Optional<CartItemEntity> cartItemEntity =
                cartItemEntityRepository.findById(cartItemEntityUpdateDto.getCartItemId());

        if (cartItemEntity.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Could not find cart item with id " + cartItemEntityUpdateDto.getCartItemId());
        } else if (cartItemEntity.get().getProductVariationDetailEntity().getQuantity() < cartItemEntityUpdateDto.getQuantity()) {
            throw new InvalidStateException(
                    "The requested item stock is smaller than the requested quantity");
        }

        cartItemEntity.get().setQuantity(cartItemEntityUpdateDto.getQuantity());
        return cartItemMapper.cartItemEntityToDto(cartItemEntityRepository.save(cartItemEntity.get()));
    }

    private void checkCorrectUser(int cartItemId) {
        UserEntity userEntity = authencationUtils.extractUserFromAuthentication();
        Optional<CartItemEntity> cartItemEntity = cartItemEntityRepository.findById(cartItemId);
        if (cartItemEntity.isEmpty() || cartItemEntity.get().getUserEntity() != userEntity) {
            throw new InvalidStateException("Not have permission to change cart item");
        }
    }

    @Override
    public void deleteCartItem(int cartItemId) {
        if (!cartItemEntityRepository.existsById(cartItemId)) {
            throw new ResourceNotFoundException("Could not find cart item with id " + cartItemId);
        }
        checkCorrectUser(cartItemId);
        cartItemEntityRepository.deleteById(cartItemId);
    }
}
