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

    // For BxGy
    @Builder.Default
    private String buyProductId = "";
    @Builder.Default
    private Integer buyQuantity = 0;
    @Builder.Default
    private String getProductId = "";
    @Builder.Default
    private Integer getQuantity = 0;

    // For Product-wise
    @Builder.Default
    private String productId = "";

    // For Cart-wise
    @Builder.Default
    private Double minimumCartValue = 0.0;
}
