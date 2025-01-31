package com.ecommerce.couponservice.service.strategy;

import com.ecommerce.couponservice.dto.AppliedCartDTO;
import com.ecommerce.couponservice.dto.CartDTO;
import com.ecommerce.couponservice.model.Coupon;
import org.springframework.stereotype.Component;
import java.util.HashMap;

@Component
public class CartCouponStrategy implements CouponStrategy {
    @Override
    public boolean isApplicable(CartDTO cart, Coupon coupon) {
        return cart.getTotalAmount() >= coupon.getMinimumCartValue();
    }

    @Override
    public AppliedCartDTO apply(CartDTO cart, Coupon coupon) {
        double discount = (cart.getTotalAmount() * coupon.getDiscount()) / 100;

        return AppliedCartDTO.builder()
                .itemDiscounts(new HashMap<>()) // No specific item discounts for cart-wise
                .totalDiscount(discount)
                .build();
    }
}