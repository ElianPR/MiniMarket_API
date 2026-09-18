package com.elian.market.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDto {
    @NotBlank(message = "La dirección de envío es obligatoria")
    private String shippingAddress;

    @NotEmpty(message = "La orden debe contener al menos un producto")
    @Valid
    private List<OrderItemRequestDto> items;
}
