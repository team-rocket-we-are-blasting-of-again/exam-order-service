package com.teamrocket.Template.service;

import com.teamrocket.Template.dto.OrderDTO;

public interface OrderService {
    public OrderDTO saveOrder(OrderDTO dto);
    public OrderDTO updateOrder(OrderDTO dto);
}
