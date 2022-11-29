package com.teamrocket.orderservice.application;

import com.teamrocket.orderservice.model.dto.OrderCancelled;
import com.teamrocket.orderservice.model.dto.OrderDTO;
import com.teamrocket.orderservice.enums.OrderStatus;
import com.teamrocket.orderservice.model.dto.RestaurantOrder;
import com.teamrocket.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaService {
    @Value("${camunda.server.engine}")
    private String restEngine;

    private RestTemplate restTemplate;

    private OrderService orderService;

    public KafkaService(OrderService orderService) {
        this.orderService = orderService;
    }

    public OrderDTO updateOrderStatus(int id, OrderStatus status) {
        OrderDTO result;
        try {
            result = orderService.updateOrderStatus(id, status);
            log.info("Order " + result.getId() + " changed status: " + status.toString());
            return result;
        } catch(Exception e) {
            log.error(e.getMessage());
            //TODO: Discuss whether order should be cancelled in this case or not
            return null;
        }
    }

    @KafkaListener(topics = "ORDER_ACCEPTED", groupId = "order-manager")
    public void orderAccepted(@Payload RestaurantOrder order) {
        updateOrderStatus(order.getId(), OrderStatus.IN_PROGRESS);
    }

    @KafkaListener(topics = "ORDER_READY", groupId = "order-manager")
    public void orderReady(@Payload RestaurantOrder order) {
        updateOrderStatus(order.getId(), OrderStatus.READY);
    }

    @KafkaListener(topics = "ORDER_PICKED_UP", groupId = "order-manager")
    public void orderPickedUp(@Payload RestaurantOrder order) {
        updateOrderStatus(order.getId(), OrderStatus.PICKED_UP);
    }

    @KafkaListener(topics = "ORDER_CANCELED", groupId = "order-manager")
    @KafkaHandler
    public void orderCancelled(@Payload OrderCancelled orderCancelled) {
        OrderDTO result = updateOrderStatus(orderCancelled.getSystemOrderId(), OrderStatus.CANCELED);
        cancelCamundaProcess(result.getProcessId());
    }

    public void cancelCamundaProcess(String instanceId) {
        restTemplate = new RestTemplate();
        try{
            String endProcessURL = new StringBuilder(restEngine)
                    .append("process-instance/")
                    .append(instanceId)
                    .toString();
            restTemplate.delete(endProcessURL);
            log.info("Process instance " + instanceId + " was cancelled");
        } catch(Exception e) {
            log.error(e.getMessage());
        }
    }
}
