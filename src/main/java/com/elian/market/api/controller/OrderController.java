package com.elian.market.api.controller;

import com.elian.market.api.domain.Order;
import com.elian.market.api.domain.OrderStatus;
import com.elian.market.api.domain.User;
import com.elian.market.api.dto.OrderRequestDto;
import com.elian.market.api.dto.OrderResponseDto;
import com.elian.market.api.dto.UpdateOrderStatusRequest;
import com.elian.market.api.mapper.OrderMapper;
import com.elian.market.api.service.IOrderService;
import com.elian.market.api.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;
    private final IUserService userService;
    private final OrderMapper orderMapper;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<OrderResponseDto> createOrder(
            @Valid @RequestBody OrderRequestDto request) {

        User currentUser = userService.getCurrentUser();

        Order order = orderService.createOrder(request, currentUser);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderMapper.toResponseDto(order));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<List<OrderResponseDto>> findMyOrders() {

        User currentUser = userService.getCurrentUser();

        List<OrderResponseDto> orders = orderService.findMyOrders(currentUser)
                .stream()
                .map(orderMapper::toResponseDto)
                .toList();

        return ResponseEntity.ok(orders);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> findById(@PathVariable Long id) {

        User currentUser = userService.getCurrentUser();

        Order order = orderService.findById(id, currentUser);

        return ResponseEntity.ok(orderMapper.toResponseDto(order));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponseDto>> findAll() {

        List<OrderResponseDto> orders = orderService.findAll()
                .stream()
                .map(orderMapper::toResponseDto)
                .toList();

        return ResponseEntity.ok(orders);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponseDto> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusRequest request) {

        OrderStatus newStatus = request.getStatus();

        Order updatedOrder = orderService.updateStatus(id, newStatus);

        return ResponseEntity.ok(orderMapper.toResponseDto(updatedOrder));
    }
}
