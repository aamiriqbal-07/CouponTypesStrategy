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
        List<AppliedCartDTO> applicableCoupons = new ArrayList<>();

        for (Coupon coupon : couponRepository.findByActiveTrue()) {
            CouponStrategy strategy = strategyMap.get(coupon.getType());
            if (strategy.isApplicable(cart, coupon)) {
                applicableCoupons.add(strategy.apply(cart, coupon));
            }
        }

        return applicableCoupons;
    }

    public AppliedCartDTO applyCoupon(Long couponId, CartDTO cart) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CouponNotFoundException("Coupon not found"));

        if (!coupon.isActive()) {
            throw new InvalidCouponException("Coupon is not active");
        }

        CouponStrategy strategy = strategyMap.get(coupon.getType());
        if (!strategy.isApplicable(cart, coupon)) {
            throw new InvalidCouponException("Coupon conditions not met");
        }

        return strategy.apply(cart, coupon);
    }

    private Coupon mapToEntity(CouponDTO dto) {
        return Coupon.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .type(dto.getType())
                .discount(dto.getDiscount())
                .threshold(dto.getThreshold())
                .description(dto.getDescription())
                .active(dto.isActive())
                .buyProductId(dto.getBuyProductId())
                .buyQuantity(dto.getBuyQuantity())
                .getProductId(dto.getGetProductId())
                .getQuantity(dto.getGetQuantity())
                .productId(dto.getProductId())
                .minimumCartValue(dto.getMinimumCartValue())
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
        existing.setCode(updated.getCode());
        existing.setType(updated.getType());
        existing.setDiscount(updated.getDiscount());
        existing.setThreshold(updated.getThreshold());
        existing.setDescription(updated.getDescription());
        existing.setActive(updated.isActive());
        existing.setBuyProductId(updated.getBuyProductId());
        existing.setBuyQuantity(updated.getBuyQuantity());
        existing.setGetProductId(updated.getGetProductId());
        existing.setGetQuantity(updated.getGetQuantity());
        existing.setProductId(updated.getProductId());
        existing.setMinimumCartValue(updated.getMinimumCartValue());
    }
}