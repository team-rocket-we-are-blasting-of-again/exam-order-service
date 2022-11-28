package com.teamrocket.orderservice.service;

import com.teamrocket.orderservice.dto.OrderDTO;

public interface KafkaService {
    void orderCancelled(int id);
    void orderReady(int id);
    void orderPickedUp(int id);
}
