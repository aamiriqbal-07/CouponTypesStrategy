package com.ecommerce.couponservice.dto;

import com.ecommerce.couponservice.model.enums.CouponType;
import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class CouponDTO {
    private Long id;
    private String code;
    private CouponType type;
    private Double discount;
    private Double threshold;
    private String description;
    private boolean active;
    private String buyProductId;
    private Integer buyQuantity;
    private String getProductId;
    private Integer getQuantity;
    private String productId;
    private Double minimumCartValue;
}