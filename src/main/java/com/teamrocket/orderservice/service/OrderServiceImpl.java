package com.teamrocket.orderservice.service;

import com.teamrocket.orderservice.dto.OrderDTO;
import com.teamrocket.orderservice.entity.Order;
import com.teamrocket.orderservice.enums.OrderStatus;
import com.teamrocket.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public OrderDTO saveOrder(OrderDTO dto) {
        Order toSave = Order.fromDto(dto);
        Order order = orderRepository.save(toSave);
        OrderDTO orderDTO = OrderDTO.fromOrder(order);
        return orderDTO;
    }

    @Override
    public OrderDTO updateOrderStatus(int orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setStatus(newStatus);
        OrderDTO orderDTO = OrderDTO.fromOrder(order);
        return orderDTO;
    }

    @Override
    public OrderDTO getOrderById(int orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        return OrderDTO.fromOrder(order);
    }
}
