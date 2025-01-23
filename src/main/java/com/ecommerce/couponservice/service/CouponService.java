package com.ecommerce.couponservice.service;

import com.ecommerce.couponservice.dto.*;
import com.ecommerce.couponservice.exception.*;
import com.ecommerce.couponservice.model.Coupon;
import com.ecommerce.couponservice.model.enums.CouponType;
import com.ecommerce.couponservice.repository.CouponRepository;
import com.ecommerce.couponservice.service.strategy.CouponStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final Map<CouponType, CouponStrategy> strategyMap;

    public CouponDTO createCoupon(CouponDTO couponDTO) {
        if (couponRepository.existsByCode(couponDTO.getCode())) {
            throw new InvalidCouponException("Coupon code already exists");
        }

        Coupon coupon = mapToEntity(couponDTO);
        coupon.setActive(true);
        return mapToDTO(couponRepository.save(coupon));
    }

    public List<CouponDTO> getAllCoupons() {
        return couponRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    public CouponDTO getCouponById(Long id) {
        return couponRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new CouponNotFoundException("Coupon not found"));
    }

    public CouponDTO updateCoupon(Long id, CouponDTO couponDTO) {
        Coupon existingCoupon = couponRepository.findById(id)
                .orElseThrow(() -> new CouponNotFoundException("Coupon not found"));

        updateCouponFields(existingCoupon, couponDTO);
        return mapToDTO(couponRepository.save(existingCoupon));
    }

    public void deleteCoupon(Long id) {
        if (!couponRepository.existsById(id)) {
            throw new CouponNotFoundException("Coupon not found");
        }
        couponRepository.deleteById(id);
    }

    public List<AppliedCartDTO> getApplicableCoupons(CartDTO cart) {
        return couponRepository.findByActiveTrue().stream()
                .map(coupon -> {
                    CouponStrategy strategy = strategyMap.get(coupon.getType());
                    if (strategy.isApplicable(cart, coupon)) {
                        double discount = strategy.apply(cart, coupon).getTotalDiscount();
                        return AppliedCartDTO.builder()
                                .couponId(coupon.getId())
                                .type(coupon.getType().name().toLowerCase())
                                .discount(discount)
                                .build();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public UpdatedCartDTO applyCoupon(Long couponId, CartDTO cart) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CouponNotFoundException("Coupon not found"));

        if (!coupon.isActive()) {
            throw new InvalidCouponException("Coupon is not active");
        }

        CouponStrategy strategy = strategyMap.get(coupon.getType());
        if (!strategy.isApplicable(cart, coupon)) {
            throw new InvalidCouponException("Coupon conditions not met");
        }

        AppliedCartDTO appliedCart = strategy.apply(cart, coupon);

        List<UpdatedCartItemDTO> updatedItems = cart.getItems().stream()
                .map(item -> UpdatedCartItemDTO.builder()
                        .productId(item.getProductId())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .totalDiscount(
                                appliedCart.getItemDiscounts().getOrDefault(item.getProductId(), 0.0))
                        .build())
                .collect(Collectors.toList());

        double totalDiscount = appliedCart.getTotalDiscount();
        double finalPrice = cart.getTotalAmount() - totalDiscount;

        return UpdatedCartDTO.builder()
                .items(updatedItems)
                .totalPrice(cart.getTotalAmount())
                .totalDiscount(totalDiscount)
                .finalPrice(finalPrice)
                .build();
    }

    private Coupon mapToEntity(CouponDTO dto) {
        return Coupon.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .type(dto.getType())
                .discount(dto.getDiscount() != null ? dto.getDiscount() : 0.0)
                .threshold(dto.getThreshold() != null ? dto.getThreshold() : 0.0)
                .description(dto.getDescription() != null ? dto.getDescription() : "")
                .active(dto.isActive())
                .buyProductId(dto.getBuyProductId() != null ? dto.getBuyProductId() : "")
                .buyQuantity(dto.getBuyQuantity() != null ? dto.getBuyQuantity() : 0)
                .getProductId(dto.getGetProductId() != null ? dto.getGetProductId() : "")
                .getQuantity(dto.getGetQuantity() != null ? dto.getGetQuantity() : 0)
                .productId(dto.getProductId() != null ? dto.getProductId() : "")
                .minimumCartValue(dto.getMinimumCartValue() != null ? dto.getMinimumCartValue() : 0.0)
                .build();
    }

    private CouponDTO mapToDTO(Coupon entity) {
        return CouponDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .type(entity.getType())
                .discount(entity.getDiscount())
                .threshold(entity.getThreshold())
                .description(entity.getDescription())
                .active(entity.isActive())
                .buyProductId(entity.getBuyProductId())
                .buyQuantity(entity.getBuyQuantity())
                .getProductId(entity.getGetProductId())
                .getQuantity(entity.getGetQuantity())
                .productId(entity.getProductId())
                .minimumCartValue(entity.getMinimumCartValue())
                .build();
    }

    private void updateCouponFields(Coupon existing, CouponDTO updated) {
        existing.setCode(updated.getCode() != null ? updated.getCode() : existing.getCode());
        existing.setType(updated.getType() != null ? updated.getType() : existing.getType());
        existing.setDiscount(updated.getDiscount() != null ? updated.getDiscount() : existing.getDiscount());
        existing.setThreshold(updated.getThreshold() != null ? updated.getThreshold() : existing.getThreshold());
        existing.setDescription(updated.getDescription() != null ? updated.getDescription() : existing.getDescription());
        existing.setActive(updated.isActive());
        existing.setBuyProductId(updated.getBuyProductId() != null ? updated.getBuyProductId() : existing.getBuyProductId());
        existing.setBuyQuantity(updated.getBuyQuantity() != null ? updated.getBuyQuantity() : existing.getBuyQuantity());
        existing.setGetProductId(updated.getGetProductId() != null ? updated.getGetProductId() : existing.getGetProductId());
        existing.setGetQuantity(updated.getGetQuantity() != null ? updated.getGetQuantity() : existing.getGetQuantity());
        existing.setProductId(updated.getProductId() != null ? updated.getProductId() : existing.getProductId());
        existing.setMinimumCartValue(updated.getMinimumCartValue() != null ? updated.getMinimumCartValue() : existing.getMinimumCartValue());
    }

}