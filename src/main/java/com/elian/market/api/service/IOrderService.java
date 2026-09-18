package com.elian.market.api.service;

import com.elian.market.api.domain.Order;
import com.elian.market.api.domain.OrderStatus;
import com.elian.market.api.domain.User;
import com.elian.market.api.dto.OrderRequestDto;

import java.util.List;

public interface IOrderService {
    Order createOrder(OrderRequestDto request, User currentUser);

    List<Order> findMyOrders(User currentUser);

    Order findById(Long id, User currentUser);

    List<Order> findAll();

    Order updateStatus(Long id, OrderStatus newStatus);
}
