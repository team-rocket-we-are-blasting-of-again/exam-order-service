package com.teamrocket.orderservice.service;

import com.teamrocket.orderservice.dto.OrderDTO;
import com.teamrocket.orderservice.enums.OrderStatus;

public interface OrderService {
    public OrderDTO saveOrder(OrderDTO dto);
    public OrderDTO updateOrderStatus(int orderId, OrderStatus newStatus);
    public OrderDTO getOrderById(int orderId);
}
