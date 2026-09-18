package com.elian.market.api.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemResponseDto {
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
}
