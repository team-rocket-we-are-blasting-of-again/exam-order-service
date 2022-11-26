package com.teamrocket.Template.service;

import com.teamrocket.Template.dto.OrderDTO;
import com.teamrocket.Template.entity.Order;
import com.teamrocket.Template.enums.OrderStatus;
import com.teamrocket.Template.repository.OrderRepository;
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
        Order order = orderRepository.save(Order.fromDto(dto));
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
