package com.ecommerce.couponservice.dto;

import lombok.Data;

@Data
public class CartItemDTO {
    private String productId;
    private Integer quantity;
    private Double price;
}