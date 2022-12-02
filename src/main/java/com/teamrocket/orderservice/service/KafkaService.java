package com.teamrocket.orderservice.service;


import com.teamrocket.orderservice.model.dto.NewOrder;
import com.teamrocket.orderservice.model.dto.OrderDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaService {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void newOrderPlaced(OrderDTO order) {
        NewOrder newOrder = new NewOrder(order);
        kafkaTemplate.send("NEW_ORDER_PLACED", newOrder);
        log.info("Published new topic: NEW_ORDER_PLACED");
    }
}
