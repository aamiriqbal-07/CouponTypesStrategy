package com.ecommerce.couponservice.dto;

import com.ecommerce.couponservice.model.enums.CouponType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class CouponDTO {
    private Long id;

    @NotNull
    private String code;

    @NotNull
    private CouponType type;

    @NotNull
    private Double discount;

    @Builder.Default
    private Double threshold = 0.0;

    @Builder.Default
    private String description = "";

    @Builder.Default
    private boolean active = true;

    // For BXGY
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
