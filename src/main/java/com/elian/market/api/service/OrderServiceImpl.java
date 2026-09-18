package com.elian.market.api.service;

import com.elian.market.api.domain.Order;
import com.elian.market.api.domain.OrderItem;
import com.elian.market.api.domain.OrderStatus;
import com.elian.market.api.domain.Product;
import com.elian.market.api.domain.User;
import com.elian.market.api.dto.OrderItemRequestDto;
import com.elian.market.api.dto.OrderRequestDto;
import com.elian.market.api.exception.InsufficientStockException;
import com.elian.market.api.exception.ResourceNotFoundException;
import com.elian.market.api.repository.OrderRepository;
import com.elian.market.api.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public Order createOrder(OrderRequestDto request, User currentUser) {

        Order order = Order.builder()
                .user(currentUser)
                .shippingAddress(request.getShippingAddress())
                .status(OrderStatus.PENDING)
                .total(BigDecimal.ZERO)
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequestDto itemRequest : request.getItems()) {

            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException(
                                    "Producto no encontrado con id: "
                                            + itemRequest.getProductId()
                            )
                    );

            if (product.getStock() < itemRequest.getQuantity()) {
                throw new InsufficientStockException(
                        "Stock insuficiente para el producto: "
                                + product.getName()
                );
            }

            BigDecimal unitPrice = product.getPrice();

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setUnitPrice(unitPrice);

            order.addItem(orderItem);

            BigDecimal subtotal = unitPrice.multiply(
                    BigDecimal.valueOf(itemRequest.getQuantity())
            );

            total = total.add(subtotal);

            product.setStock(
                    product.getStock() - itemRequest.getQuantity()
            );
        }

        order.setTotal(total);

        return orderRepository.save(order);
    }

    @Override
    public List<Order> findMyOrders(User currentUser) {
        return orderRepository.findByUserId(currentUser.getId());
    }

    @Override
    public Order findById(Long id, User currentUser) {

        Order order = orderRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Orden no encontrada con id: " + id
                        )
                );

        boolean isOwner = order.getUser().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase("ADMIN"));

        if (!isOwner && !isAdmin) {
            // No revelamos que la orden existe si no le pertenece al usuario
            throw new ResourceNotFoundException(
                    "Orden no encontrada con id: " + id
            );
        }

        return order;
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    @Transactional
    public Order updateStatus(Long id, OrderStatus newStatus) {

        Order order = orderRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Orden no encontrada con id: " + id
                        )
                );

        validateStatusTransition(order.getStatus(), newStatus);

        order.setStatus(newStatus);

        return orderRepository.save(order);
    }

    private void validateStatusTransition(
            OrderStatus currentStatus,
            OrderStatus newStatus
    ) {

        if (currentStatus == OrderStatus.CANCELLED) {
            throw new IllegalArgumentException(
                    "Una orden cancelada no puede cambiar de estado"
            );
        }

        if (currentStatus == OrderStatus.SHIPPED) {
            throw new IllegalArgumentException(
                    "Una orden enviada no puede cambiar de estado"
            );
        }

        if (currentStatus == OrderStatus.PENDING
                && newStatus == OrderStatus.SHIPPED) {

            throw new IllegalArgumentException(
                    "Una orden pendiente debe pagarse antes de enviarse"
            );
        }
    }
}