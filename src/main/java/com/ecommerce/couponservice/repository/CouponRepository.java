package com.ecommerce.couponservice.repository;

import com.ecommerce.couponservice.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    List<Coupon> findByActiveTrue();
    boolean existsByCode(String code);
}