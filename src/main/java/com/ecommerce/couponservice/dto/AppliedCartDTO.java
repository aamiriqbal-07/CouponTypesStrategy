package com.ecommerce.couponservice.dto;

import lombok.Data;
import lombok.Builder;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class AppliedCartDTO {
    private List<CartItemDTO> items;
    private Double originalTotal;
    private Double discountedTotal;
    private Double totalDiscount;
    private Map<String, Double> itemDiscounts;
}