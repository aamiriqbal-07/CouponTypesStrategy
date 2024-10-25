package com.ecommerce.couponservice.model;

import com.ecommerce.couponservice.model.enums.CouponType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coupons")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CouponType type;

    private String code;
    private Double discount;
    private Double threshold;
    private String description;
    private boolean active;

    // For BXGY
    private String buyProductId;
    private Integer buyQuantity;
    private String getProductId;
    private Integer getQuantity;

    // For Product-wise
    private String productId;

    // For Cart-wise
    private Double minimumCartValue;
}