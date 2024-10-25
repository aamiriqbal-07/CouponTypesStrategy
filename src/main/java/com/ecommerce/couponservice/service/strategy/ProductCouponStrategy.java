package com.ecommerce.couponservice.service.strategy;

import com.ecommerce.couponservice.dto.AppliedCartDTO;
import com.ecommerce.couponservice.dto.CartDTO;
import com.ecommerce.couponservice.dto.CartItemDTO;
import com.ecommerce.couponservice.model.Coupon;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ProductCouponStrategy implements CouponStrategy {
    @Override
    public boolean isApplicable(CartDTO cart, Coupon coupon) {
        return cart.getItems().stream()
                .anyMatch(item -> item.getProductId().equals(coupon.getProductId()));
    }

    @Override
    public AppliedCartDTO apply(CartDTO cart, Coupon coupon) {
        Map<String, Double> itemDiscounts = new HashMap<>();
        double totalDiscount = 0;

        for (CartItemDTO item : cart.getItems()) {
            if (item.getProductId().equals(coupon.getProductId())) {
                double itemDiscount = (item.getPrice() * item.getQuantity() * coupon.getDiscount()) / 100;
                itemDiscounts.put(item.getProductId(), itemDiscount);
                totalDiscount += itemDiscount;
            }
        }

        return AppliedCartDTO.builder()
                .items(cart.getItems())
                .originalTotal(cart.getTotalAmount())
                .discountedTotal(cart.getTotalAmount() - totalDiscount)
                .totalDiscount(totalDiscount)
                .itemDiscounts(itemDiscounts)
                .build();
    }
}