package com.elian.market.api.dto;

import com.elian.market.api.domain.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOrderStatusRequest {
    @NotNull(message = "El estado es obligatorio")
    private OrderStatus status;
}
