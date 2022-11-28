package com.teamrocket.orderservice.service;

import com.teamrocket.orderservice.dto.OrderDTO;
import com.teamrocket.orderservice.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaServiceImpl implements KafkaService {
    @Value("${camunda.server.engine}")
    private String restEngine;

    private RestTemplate restTemplate;

    @Autowired
    private OrderService orderService;

    @Override
    @KafkaListener(topics = "ORDER_READY", groupId = "order-status")
    public void orderReady(int id) {
        OrderDTO result;
        try {
            result = orderService.updateOrderStatus(id, OrderStatus.READY);
            log.info("Order " + result.getId() + " changed status: READY");
        } catch(Exception e) {
            log.error(e.getMessage());
            //TODO: Discuss whether order should be cancelled in this case or not
        }
    }

    @Override
    @KafkaListener(topics = "ORDER_PICKED_UP", groupId = "order-status")
    public void orderPickedUp(int id) {
        OrderDTO result;
        try {
            result = orderService.updateOrderStatus(id, OrderStatus.PICKED_UP);
            log.info("Order " + result.getId() + " changed status: PICKED_UP");
        } catch(Exception e) {
            log.error(e.getMessage());
            //TODO: Discuss whether order should be cancelled in this case or not
        }
    }

    @Override
    @KafkaListener(topics = "ORDER_CANCELED", groupId = "order-status")
    @KafkaHandler
    public void orderCancelled(int id) {
        OrderDTO result = orderService.updateOrderStatus(id, OrderStatus.CANCELED);
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
