package com.example.springbootecommerce.service.implementations;

import com.example.springbootecommerce.constant.OrderStatus;
import com.example.springbootecommerce.dto.orderentity.OrderEntityCreateDto;
import com.example.springbootecommerce.dto.orderentity.OrderEntityIndexDto;
import com.example.springbootecommerce.exception.InvalidStateException;
import com.example.springbootecommerce.exception.ResourceDuplicateException;
import com.example.springbootecommerce.exception.ResourceNotFoundException;
import com.example.springbootecommerce.mapper.orderentity.OrderMapper;
import com.example.springbootecommerce.model.*;
import com.example.springbootecommerce.repository.CartItemEntityRepository;
import com.example.springbootecommerce.repository.DiscountEntityRepository;
import com.example.springbootecommerce.repository.OrderEntityRepository;
import com.example.springbootecommerce.repository.OrderItemEntityRepository;
import com.example.springbootecommerce.service.interfaces.OrderServiceInterface;
import com.example.springbootecommerce.util.AuthencationUtils;
import com.example.springbootecommerce.util.DateUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderServiceInterface {
    private final AuthencationUtils authencationUtils;
    private final OrderEntityRepository orderEntityRepository;
    private final CartItemEntityRepository cartItemEntityRepository;
    private final DiscountEntityRepository discountEntityRepository;
    private final OrderItemEntityRepository orderItemEntityRepository;
    private final OrderMapper orderMapper;
    private final DateUtil dateUtil;

    @Override
    @Transactional
    public OrderEntityIndexDto createOrder(OrderEntityCreateDto orderEntityCreateDto) {
        UserEntity userEntity = authencationUtils.extractUserFromAuthentication();
        Set<Integer> cartItemIds = orderEntityCreateDto.getCartItemIds();
        Set<Integer> discountIds = orderEntityCreateDto.getDiscountIds();

        AtomicReference<Integer> totalPrice = new AtomicReference<>(0);
        // Step 1: Create OrderItemEntities from CartItemEntities
        Set<OrderItemEntity> orderItems = cartItemIds.stream()
                .map(cartItemId -> {
                    // Logic to fetch CartItemEntity based on cartItemId and userEntity
                    CartItemEntity cartItemEntity = cartItemEntityRepository.findByIdAndUserEntity(cartItemId, userEntity)
                            .orElseThrow(() -> new ResourceNotFoundException("Could not find cart item with id " + cartItemId));
                    validateCartItemEntity(cartItemEntity);
                    totalPrice.updateAndGet(v -> v + cartItemEntity.getQuantity() * cartItemEntity.getProductVariationDetailEntity().getPrice());
                    // Create OrderItemEntity from CartItemEntity
                    return OrderItemEntity.builder()
                            .quantity(cartItemEntity.getQuantity())
                            .price(cartItemEntity.getProductVariationDetailEntity().getPrice())
                            .productEntity(cartItemEntity.getProductEntity())
                            .productVariationDetailEntity(cartItemEntity.getProductVariationDetailEntity())
                            .build();
                })
                .collect(Collectors.toSet());

        // Step 2: Create the OrderEntity and associate OrderItemEntities
        OrderEntity orderEntity = orderMapper.orderEntityCreateDtoToEntity(orderEntityCreateDto);
        orderEntity.setOrderStatus(OrderStatus.PENDING);
        orderEntity.setUserEntity(userEntity);
        orderEntity.setTotalPrice(totalPrice.get());

        OrderEntity finalOrderEntity = orderEntity;
        orderItems.forEach(orderItem -> orderItem.setOrderEntity(finalOrderEntity));
        orderItemEntityRepository.saveAll(orderItems);

        // Add OrderItemEntities to the OrderEntity
        orderEntity.setOrderItemEntityList(orderItems);

        // Save the OrderEntity to the database
        orderEntity = orderEntityRepository.save(orderEntity);


        // Step 3: Associate selected discounts with the OrderEntity
        AtomicReference<Integer> totalDiscount = new AtomicReference<>(0);
        Set<DiscountEntity> discounts = discountIds.stream()
                .map(discountId -> {
                    // Logic to fetch DiscountEntity based on discountId
                    DiscountEntity discountEntity = discountEntityRepository.findById(discountId)
                            .orElseThrow(() -> new ResourceNotFoundException("Could not find discount with id " + discountId));

                    validateDiscountEntity(discountEntity);
                    totalDiscount.updateAndGet(v -> v + getTotalDiscountFromListProductInCart(discountEntity, orderItems));
                    return discountEntity;

                })
                .collect(Collectors.toSet());

        // Associate discounts with the order
        orderEntity.setDiscountEntities(discounts);
        orderEntity.setTotalDiscount(totalDiscount.get());
        orderEntity.setTotalCheckout(totalPrice.get() - totalDiscount.get());
        orderEntity = orderEntityRepository.save(orderEntity);

        // Convert OrderEntity to OrderEntityIndexDto and return
        return orderMapper.orderEntityToIndexDto(orderEntity);
    }

    private void validateDiscountEntity(DiscountEntity discountEntity) {
        if (!discountEntity.isDiscountIsActive()) {
            throw new InvalidStateException("Discount not active with id " + discountEntity.getId());
        }

        LocalDate localDate = LocalDate.now();
        if (localDate.isAfter(dateUtil.convertToLocalDate(discountEntity.getDiscountEndDate())) || localDate.isBefore(dateUtil.convertToLocalDate(discountEntity.getDiscountStartDate()))) {
            throw new InvalidStateException("Discount expired with id " + discountEntity.getId());
        }

        if (discountEntity.getDiscountMaxUses() <= 0) {
            throw new ResourceDuplicateException("Discount are out with id " + discountEntity.getId());
        }
    }

    private Integer getTotalDiscountFromListProductInCart(DiscountEntity discountEntity, Set<OrderItemEntity> orderItems) {
        if (discountEntity.getDiscountType().equals("fixed_amount")) {
            return discountEntity.getDiscountValue();
        }

        if (discountEntity.getDiscountAppliesTo().equals("all")) {
            int totalItemPrice = 0;
            for (OrderItemEntity orderItem : orderItems) {
                totalItemPrice += orderItem.getQuantity() * orderItem.getPrice();
            }
            return (int) Math.ceil(totalItemPrice * (discountEntity.getDiscountValue() / 100.0));
        } else {
            Set<Integer> discountProductIds = discountEntity.getDiscountProducts();

            int discountedItemPrice = 0;
            for (OrderItemEntity orderItem : orderItems) {
                if (discountProductIds.contains(orderItem.getProductEntity().getId())) {
                    discountedItemPrice += orderItem.getQuantity() * orderItem.getPrice();
                }
            }

            return (int) Math.ceil(discountedItemPrice * (discountEntity.getDiscountValue() / 100.0));
        }
    }


    private void validateCartItemEntity(CartItemEntity cartItemEntity) {
        int cartItemQuantity = cartItemEntity.getQuantity();
        int availableQuantity = cartItemEntity.getProductVariationDetailEntity().getQuantity();

        if (cartItemQuantity > availableQuantity) {
            throw new IllegalArgumentException("Quantity in the cart exceeds available quantity for cart item with id " + cartItemEntity.getId());
        }
    }

}
