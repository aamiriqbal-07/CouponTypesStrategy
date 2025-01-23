package com.ecommerce.couponservice.dto;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class UpdatedCartItemDTO {
    private String productId;
    private Integer quantity;
    private Double price;
    private Double totalDiscount;
}
