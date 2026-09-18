package com.elian.market.api.dto;

import com.elian.market.api.domain.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDto {
    private Long id;
    private OrderStatus status;
    private BigDecimal total;
    private String shippingAddress;
    private LocalDateTime createdAt;
    private List<OrderItemResponseDto> items;
}
