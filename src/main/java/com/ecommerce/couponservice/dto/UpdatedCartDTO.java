package com.ecommerce.couponservice.dto;

import lombok.Data;
import lombok.Builder;

import java.util.List;

@Data
@Builder
public class UpdatedCartDTO {
    private List<UpdatedCartItemDTO> items;
    private Double totalPrice;
    private Double totalDiscount;
    private Double finalPrice;
}
