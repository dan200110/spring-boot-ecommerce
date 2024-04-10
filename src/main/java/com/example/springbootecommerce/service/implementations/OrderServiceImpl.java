package com.example.springbootecommerce.service.implementations;

import com.example.springbootecommerce.constant.OrderStatus;
import com.example.springbootecommerce.dto.discountentity.DiscountEntityIndexDto;
import com.example.springbootecommerce.dto.orderentity.OrderEntityCreateDto;
import com.example.springbootecommerce.dto.orderentity.OrderEntityIndexDto;
import com.example.springbootecommerce.dto.orderentity.OrderItemEntityDto;
import com.example.springbootecommerce.exception.InvalidStateException;
import com.example.springbootecommerce.exception.ResourceDuplicateException;
import com.example.springbootecommerce.exception.ResourceNotFoundException;
import com.example.springbootecommerce.mapper.discountentity.DiscountMapper;
import com.example.springbootecommerce.mapper.orderentity.OrderItemMapper;
import com.example.springbootecommerce.mapper.orderentity.OrderMapper;
import com.example.springbootecommerce.model.*;
import com.example.springbootecommerce.repository.*;
import com.example.springbootecommerce.service.interfaces.CartItemServiceInterface;
import com.example.springbootecommerce.service.interfaces.OrderServiceInterface;
import com.example.springbootecommerce.util.AuthencationUtils;
import com.example.springbootecommerce.util.DateUtil;
import jakarta.persistence.LockModeType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
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
    private final OrderItemMapper orderItemMapper;
    private final DiscountMapper discountMapper;
    private final ProductVariationDetailRepository productVariationDetailRepository;
    private final CartItemServiceInterface cartItemServiceInterface;
    private final DiscountUsageEntityRepository discountUsageEntityRepository;

    @Override
    public Page<OrderEntityIndexDto> getAllOrder(Pageable pageable) {
        Page<OrderEntity> orderEntities = orderEntityRepository.findAll(pageable);
        return orderEntities.map(this::mapEntityToIndexDto);
    }

    @Override
    @Transactional
    public OrderEntityIndexDto createOrder(OrderEntityCreateDto orderEntityCreateDto) {
        UserEntity userEntity = authencationUtils.extractUserFromAuthentication();
        Set<Integer> cartItemIds = orderEntityCreateDto.getCartItemIds();
        Set<Integer> discountIds = orderEntityCreateDto.getDiscountIds();

        Set<OrderItemEntity> orderItems = createOrderItems(userEntity, cartItemIds);

        OrderEntity orderEntity = buildOrderEntity(userEntity, orderItems, orderEntityCreateDto);

        Set<DiscountEntity> discounts = processDiscounts(orderEntity, discountIds);
        orderEntity.setDiscountEntities(discounts);

        orderEntity = orderEntityRepository.save(orderEntity);
        decreaseProductQuantity(orderItems);
        removeItemsFromCart(cartItemIds);
        decreaseDiscountUsed(discounts, userEntity);

        return mapEntityToIndexDto(orderEntity);
    }

    private void decreaseDiscountUsed(Set<DiscountEntity> discounts, UserEntity userEntity) {
        for (DiscountEntity discountEntity : discounts) {
            // Decrease discountUsedCount by 1
            int updatedMaxUseCount = discountEntity.getDiscountMaxUses() - 1;
            if (updatedMaxUseCount < 0) {
                throw new InvalidStateException("Discount cannot be used: " + discountEntity.getId());
            }
            discountEntity.setDiscountUsedCount(discountEntity.getDiscountUsedCount() + 1);
            discountEntity.setDiscountMaxUses(updatedMaxUseCount);
            discountEntityRepository.save(discountEntity); // Saving the updated discount entity

            // Check if there is an existing DiscountUsageEntity for this user and discount
            DiscountUsageEntity existingUsage = discountUsageEntityRepository.findByDiscountEntityAndUserEntity(discountEntity, userEntity);
            if (existingUsage != null) {
                // Increment usageCount by 1
                existingUsage.setUsageCount(existingUsage.getUsageCount() + 1);
                discountUsageEntityRepository.save(existingUsage); // Saving the updated usage entity
            } else {
                // Create and save new DiscountUsageEntity
                DiscountUsageEntity discountUsageEntity = new DiscountUsageEntity();
                discountUsageEntity.setDiscountEntity(discountEntity);
                discountUsageEntity.setUserEntity(userEntity);
                discountUsageEntity.setUsageCount(1); // Set usage count to 1 for new entry
                discountUsageEntityRepository.save(discountUsageEntity);
            }
        }
    }


    private void removeItemsFromCart(Set<Integer> cartItemIds) {
        for (int cartItemId : cartItemIds) {
            cartItemServiceInterface.deleteCartItem(cartItemId);
        }
    }

    @Transactional
    public void decreaseProductQuantity(Set<OrderItemEntity> orderItems) {
        for (OrderItemEntity orderItemEntity : orderItems) {
            ProductVariationDetailEntity productVariationDetailEntity = orderItemEntity.getProductVariationDetailEntity();
            ProductVariationDetailEntity lockedProductVariationDetailEntity = productVariationDetailRepository.findWithLockingById(productVariationDetailEntity.getId());

            int quantityToDecrease = orderItemEntity.getQuantity();
            int remainingQuantity = lockedProductVariationDetailEntity.getQuantity() - quantityToDecrease;
            if (remainingQuantity < 0) {
                throw new InvalidStateException("Insufficient quantity for product variation: " + lockedProductVariationDetailEntity.getId());
            }

            lockedProductVariationDetailEntity.setQuantity(remainingQuantity);
            productVariationDetailRepository.save(lockedProductVariationDetailEntity);
        }
    }

    private Set<OrderItemEntity> createOrderItems(UserEntity userEntity, Set<Integer> cartItemIds) {
        return cartItemIds.stream()
                .map(cartItemId -> {
                    CartItemEntity cartItemEntity = getCartItemEntity(cartItemId, userEntity);
                    validateCartItemEntity(cartItemEntity);

                    return buildOrderItemFromCartItem(cartItemEntity);
                })
                .collect(Collectors.toSet());
    }

    private OrderItemEntity buildOrderItemFromCartItem(CartItemEntity cartItemEntity) {
        return OrderItemEntity.builder()
                .quantity(cartItemEntity.getQuantity())
                .price(cartItemEntity.getProductVariationDetailEntity().getPrice())
                .productEntity(cartItemEntity.getProductEntity())
                .productVariationDetailEntity(cartItemEntity.getProductVariationDetailEntity())
                .build();
    }

    private OrderEntity buildOrderEntity(UserEntity userEntity, Set<OrderItemEntity> orderItems, OrderEntityCreateDto orderEntityCreateDto) {
        OrderEntity orderEntity = orderMapper.orderEntityCreateDtoToEntity(orderEntityCreateDto);
        orderEntity.setOrderStatus(OrderStatus.PENDING);
        orderEntity.setUserEntity(userEntity);
        orderEntity.setTotalPrice(calculateTotalPrice(orderItems));
        orderEntity.setOrderItemEntityList(orderItems);
        return orderEntity;
    }

    private int calculateTotalPrice(Set<OrderItemEntity> orderItems) {
        return orderItems.stream()
                .mapToInt(orderItem -> orderItem.getQuantity() * orderItem.getPrice())
                .sum();
    }

    private Set<DiscountEntity> processDiscounts(OrderEntity orderEntity, Set<Integer> discountIds) {
        return discountIds.stream()
                .map(discountId -> {
                    DiscountEntity discountEntity = getDiscountEntity(discountId);
                    validateDiscountEntity(discountEntity);
                    int totalDiscount = getTotalDiscountFromListProductInCart(discountEntity, orderEntity.getOrderItemEntityList());
                    orderEntity.setTotalDiscount(orderEntity.getTotalDiscount() + totalDiscount);
                    return discountEntity;
                })
                .collect(Collectors.toSet());
    }

    private DiscountEntity getDiscountEntity(Integer discountId) {
        return discountEntityRepository.findById(discountId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find discount with id " + discountId));
    }

    private CartItemEntity getCartItemEntity(Integer cartItemId, UserEntity userEntity) {
        return cartItemEntityRepository.findByIdAndUserEntity(cartItemId, userEntity)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find cart item with id " + cartItemId));
    }

    private OrderEntityIndexDto mapEntityToIndexDto(OrderEntity orderEntity) {
        OrderEntityIndexDto orderEntityIndexDto = orderMapper.orderEntityToIndexDto(orderEntity);
        Set<OrderItemEntityDto> orderItemEntityDtos = orderMapper.mapOrderItemsToDtos(orderEntity.getOrderItemEntityList());
        Set<DiscountEntityIndexDto> discountEntityIndexDtos = orderMapper.mapDiscountsToDtos(orderEntity.getDiscountEntities());
        orderEntityIndexDto.setOrderItems(orderItemEntityDtos);
        orderEntityIndexDto.setDiscounts(discountEntityIndexDtos);
        return orderEntityIndexDto;
    }

    private void validateCartItemEntity(CartItemEntity cartItemEntity) {
        int cartItemQuantity = cartItemEntity.getQuantity();
        int availableQuantity = cartItemEntity.getProductVariationDetailEntity().getQuantity();

        if (cartItemQuantity > availableQuantity) {
            throw new IllegalArgumentException("Quantity in the cart exceeds available quantity for cart item with id " + cartItemEntity.getId());
        }
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

        // need to validate user used max time
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
}
