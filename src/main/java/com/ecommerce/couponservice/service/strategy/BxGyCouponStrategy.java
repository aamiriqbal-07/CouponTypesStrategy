package com.ecommerce.couponservice.service.strategy;

import com.ecommerce.couponservice.dto.AppliedCartDTO;
import com.ecommerce.couponservice.dto.CartDTO;
import com.ecommerce.couponservice.dto.CartItemDTO;
import com.ecommerce.couponservice.model.Coupon;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BxGyCouponStrategy implements CouponStrategy {
    @Override
    public boolean isApplicable(CartDTO cart, Coupon coupon) {
        return cart.getItems().stream()
                .anyMatch(item -> item.getProductId().equals(coupon.getBuyProductId())
                        && item.getQuantity() >= coupon.getBuyQuantity());
    }

    @Override
    public AppliedCartDTO apply(CartDTO cart, Coupon coupon) {
        Map<String, Double> itemDiscounts = new HashMap<>();
        double totalDiscount = 0;

        CartItemDTO getFreeItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(coupon.getGetProductId()))
                .findFirst()
                .orElse(null);

        if (getFreeItem != null) {
            int freeQuantity = Math.min(getFreeItem.getQuantity(), coupon.getGetQuantity());
            double itemDiscount = getFreeItem.getPrice() * freeQuantity;
            itemDiscounts.put(getFreeItem.getProductId(), itemDiscount);
            totalDiscount = itemDiscount;
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
