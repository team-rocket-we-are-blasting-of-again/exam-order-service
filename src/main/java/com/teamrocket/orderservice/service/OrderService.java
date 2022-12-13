package com.teamrocket.orderservice.service;

import com.teamrocket.orderservice.model.dto.OrderDTO;
import com.teamrocket.orderservice.enums.OrderStatus;

public interface OrderService {
    OrderDTO saveOrder(OrderDTO dto);
    OrderDTO updateOrderStatus(int orderId, OrderStatus newStatus);
    OrderDTO getOrderById(int orderId);
}
