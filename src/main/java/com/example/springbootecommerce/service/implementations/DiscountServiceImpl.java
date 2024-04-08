package com.example.springbootecommerce.service.implementations;

import com.example.springbootecommerce.dto.discountentity.DiscountEntityCreateDto;
import com.example.springbootecommerce.dto.discountentity.DiscountEntityDetailDto;
import com.example.springbootecommerce.dto.discountentity.DiscountEntityIndexDto;
import com.example.springbootecommerce.dto.productentity.ProductEntityAfterCreatedDto;
import com.example.springbootecommerce.exception.InvalidStateException;
import com.example.springbootecommerce.exception.ResourceDuplicateException;
import com.example.springbootecommerce.exception.ResourceNotFoundException;
import com.example.springbootecommerce.mapper.discountentity.DiscountMapper;
import com.example.springbootecommerce.mapper.productentity.ProductMapper;
import com.example.springbootecommerce.mapper.userentity.UserEntityIndexDtoMapper;
import com.example.springbootecommerce.model.DiscountEntity;
import com.example.springbootecommerce.model.ProductEntity;
import com.example.springbootecommerce.model.UserEntity;
import com.example.springbootecommerce.repository.DiscountEntityRepository;
import com.example.springbootecommerce.repository.ProductEntityRepository;
import com.example.springbootecommerce.service.interfaces.DiscountServiceInterface;
import com.example.springbootecommerce.util.AuthencationUtils;
import com.example.springbootecommerce.util.DateUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Getter
@Setter
@RequiredArgsConstructor
@Transactional
public class DiscountServiceImpl implements DiscountServiceInterface {
    private final DiscountEntityRepository discountEntityRepository;
    private final DiscountMapper discountMapper;
    private final AuthencationUtils authencationUtils;
    private final DateUtil dateUtil;
    private final ProductEntityRepository productEntityRepository;
    private final ProductMapper productMapper;
    private final UserEntityIndexDtoMapper userMapper;

    @Override
    @Transactional
    public DiscountEntityIndexDto createDiscount(DiscountEntityCreateDto discountEntityCreateDto) {
        validateDiscount(discountEntityCreateDto);
        UserEntity userEntity = authencationUtils.extractUserFromAuthentication();

        DiscountEntity discountEntity = discountMapper.toEntity(discountEntityCreateDto);
        discountEntity.setShopEntity(userEntity);
        DiscountEntity savedDiscount = discountEntityRepository.save(discountEntity);
        return discountMapper.toIndexDto(savedDiscount);
    }

    @Override
    public Page<DiscountEntityIndexDto> listDiscountOfCurrentShop(Pageable pageable) {
        UserEntity userEntity = authencationUtils.extractUserFromAuthentication();
        return discountEntityRepository.findByShopEntityId(userEntity.getId(), pageable)
                .map(discountMapper::toIndexDto);
    }

    @Override
    public DiscountEntityDetailDto getDiscountDetail(int id) {
        DiscountEntity discountEntity = discountEntityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Discount not found with id: " + id));

        Set<ProductEntityAfterCreatedDto> productEntityAfterCreatedDtos;
        if ("all".equals(discountEntity.getDiscountAppliesTo())) {
            // Find all productEntity of this ShopEntity
            int shopId = discountEntity.getShopEntity().getId(); // Assuming getId() returns the ID of the shop
            List<ProductEntity> shopProducts = productEntityRepository.findByShopEntityId(shopId);
            // Convert to Set<ProductEntityAfterCreatedDto>
            productEntityAfterCreatedDtos = shopProducts.stream()
                    .map(productMapper::toAfterCreatedDto)
                    .collect(Collectors.toSet());
        } else {
            Set<Integer> discountProductIds = discountEntity.getDiscountProducts();
            // Convert Set<Integer> to Set<ProductEntityAfterCreatedDto>
            productEntityAfterCreatedDtos = discountProductIds.stream()
                    .map(productId -> {
                        // Logic to fetch ProductEntityAfterCreatedDto based on productId
                        ProductEntity productEntity = productEntityRepository.findById(productId)
                                .orElseThrow(() -> new ResourceNotFoundException("Could not find product with id " + productId));
                        return productMapper.toAfterCreatedDto(productEntity);
                    })
                    .collect(Collectors.toSet());
        }

        DiscountEntityDetailDto discountEntityDetailDto = discountMapper.toDetailDto(discountEntity);
        discountEntityDetailDto.setDiscountProducts(productEntityAfterCreatedDtos);
        return discountEntityDetailDto;
    }

    private void validateDiscount(DiscountEntityCreateDto discountEntityCreateDto) {
        LocalDate localDate = LocalDate.now();
        if (localDate.isAfter(dateUtil.convertToLocalDate(discountEntityCreateDto.getDiscountEndDate()))) {
            throw new InvalidStateException("Invalid endDate");
        }

        if (discountEntityRepository.findByDiscountCode(discountEntityCreateDto.getDiscountCode()).isPresent()) {
            throw new ResourceDuplicateException("Discount already exists");
        }

        UserEntity userEntity = authencationUtils.extractUserFromAuthentication();
        Set<Integer> discountProducts = discountEntityCreateDto.getDiscountProducts();
        if (discountEntityCreateDto.getDiscountAppliesTo() == "specific") {
            // Check if all products in discountProducts belong to the userEntity
            for (Integer productId : discountProducts) {
                ProductEntity productEntity = productEntityRepository.findById(productId)
                        .orElseThrow(() -> new ResourceNotFoundException("Could not find product with id " + productId));
                if (!Objects.equals(productEntity.getShopEntity(), userEntity)) {
                    throw new ResourceNotFoundException("Product with ID " + productId + " does not belong to the user.");
                }
            }
        }
    }
}