// CouponServiceApplication.java
package com.ecommerce.couponservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.ecommerce.couponservice.model.enums.CouponType;
import com.ecommerce.couponservice.service.strategy.CouponStrategy;
import com.ecommerce.couponservice.service.strategy.CartCouponStrategy;
import com.ecommerce.couponservice.service.strategy.ProductCouponStrategy;
import com.ecommerce.couponservice.service.strategy.BxGyCouponStrategy;

import java.util.Map;
import java.util.HashMap;

@SpringBootApplication
public class CouponServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CouponServiceApplication.class, args);
    }

    @Bean
    public Map<CouponType, CouponStrategy> strategyMap(
            CartCouponStrategy cartCouponStrategy,
            ProductCouponStrategy productCouponStrategy,
            BxGyCouponStrategy bxGyCouponStrategy
    ) {
        Map<CouponType, CouponStrategy> strategyMap = new HashMap<>();
        strategyMap.put(CouponType.CART_WISE, cartCouponStrategy);
        strategyMap.put(CouponType.PRODUCT_WISE, productCouponStrategy);
        strategyMap.put(CouponType.BXGY, bxGyCouponStrategy);
        return strategyMap;
    }
}