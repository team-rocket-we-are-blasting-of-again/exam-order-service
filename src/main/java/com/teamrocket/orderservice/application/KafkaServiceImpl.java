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
public class KafkaServiceImpl implements KafkaService {
    @Value("${camunda.server.engine}")
    private String restEngine;

    private RestTemplate restTemplate;

    private OrderService orderService;

    public KafkaServiceImpl(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    @KafkaListener(topics = "ORDER_ACCEPTED", groupId = "order-manager")
    public void orderAccepted(@Payload RestaurantOrder order) {
        OrderDTO result;
        try {
            result = orderService.updateOrderStatus(order.getId(), OrderStatus.IN_PROGRESS);
            log.info("Order " + result.getId() + " changed status: IN_PROGRESS");
        } catch(Exception e) {
            log.error(e.getMessage());
            //TODO: Discuss whether order should be cancelled in this case or not
        }
    }

    @Override
    @KafkaListener(topics = "ORDER_READY", groupId = "order-manager")
    public void orderReady(@Payload RestaurantOrder order) {
        OrderDTO result;
        try {
            result = orderService.updateOrderStatus(order.getId(), OrderStatus.READY);
            log.info("Order " + result.getId() + " changed status: READY");
        } catch(Exception e) {
            log.error(e.getMessage());
            //TODO: Discuss whether order should be cancelled in this case or not
        }
    }

    @Override
    @KafkaListener(topics = "ORDER_PICKED_UP", groupId = "order-manager")
    public void orderPickedUp(@Payload RestaurantOrder order) {
        OrderDTO result;
        try {
            result = orderService.updateOrderStatus(order.getId(), OrderStatus.PICKED_UP);
            log.info("Order " + result.getId() + " changed status: PICKED_UP");
        } catch(Exception e) {
            log.error(e.getMessage());
            //TODO: Discuss whether order should be cancelled in this case or not
        }
    }

    @Override
    @KafkaListener(topics = "ORDER_CANCELED", groupId = "order-manager")
    @KafkaHandler
    public void orderCancelled(@Payload OrderCancelled orderCancelled) {
        OrderDTO result = orderService.updateOrderStatus(orderCancelled.getSystemOrderId(), OrderStatus.CANCELED);
        log.info("Order " + result.getId() + " changed status: CANCELLED");
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
