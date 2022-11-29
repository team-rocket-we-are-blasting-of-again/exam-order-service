package com.teamrocket.orderservice.application;

import com.teamrocket.orderservice.model.dto.OrderCancelled;
import com.teamrocket.orderservice.model.dto.RestaurantOrder;

public interface KafkaService {
    void orderCancelled(OrderCancelled orderCancelled);
    void orderReady(RestaurantOrder order);
    void orderAccepted(RestaurantOrder order);
    void orderPickedUp(RestaurantOrder order);
}
