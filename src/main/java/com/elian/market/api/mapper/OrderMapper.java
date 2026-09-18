package com.elian.market.api.mapper;

import com.elian.market.api.domain.Order;
import com.elian.market.api.domain.OrderItem;
import com.elian.market.api.dto.OrderItemResponseDto;
import com.elian.market.api.dto.OrderResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "items", source = "items")
    OrderResponseDto toResponseDto(Order order);

    @Mapping(target = "productName", source = "product.name")
    @Mapping(
            target = "subtotal",
            expression = "java(orderItem.getUnitPrice().multiply(java.math.BigDecimal.valueOf(orderItem.getQuantity())))"
    )
    OrderItemResponseDto toItemResponseDto(OrderItem orderItem);
}
