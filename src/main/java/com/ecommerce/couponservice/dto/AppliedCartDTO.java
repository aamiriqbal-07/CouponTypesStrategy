package com.ecommerce.couponservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Builder;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class AppliedCartDTO {
    private Long couponId;
    private String type;
    private Double discount;
    private Map<String, Double> itemDiscounts;

    @JsonIgnore
    private double totalDiscount;
}
