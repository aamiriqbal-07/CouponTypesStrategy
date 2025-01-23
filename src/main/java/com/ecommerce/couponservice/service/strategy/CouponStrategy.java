package com.ecommerce.couponservice.service.strategy;

import com.ecommerce.couponservice.dto.AppliedCartDTO;
import com.ecommerce.couponservice.dto.CartDTO;
import com.ecommerce.couponservice.model.Coupon;

public interface CouponStrategy {
    boolean isApplicable(CartDTO cart, Coupon coupon);
    AppliedCartDTO apply(CartDTO cart, Coupon coupon);
}