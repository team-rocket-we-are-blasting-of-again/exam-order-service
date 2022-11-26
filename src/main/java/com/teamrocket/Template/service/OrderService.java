package com.teamrocket.Template.service;

import com.teamrocket.Template.dto.OrderDTO;
import com.teamrocket.Template.enums.OrderStatus;

public interface OrderService {
    public OrderDTO saveOrder(OrderDTO dto);
    public OrderDTO updateOrderStatus(int orderId, OrderStatus newStatus);
    public OrderDTO getOrderById(int orderId);
}
