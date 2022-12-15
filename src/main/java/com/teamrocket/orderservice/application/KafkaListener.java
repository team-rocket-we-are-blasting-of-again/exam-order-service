package com.teamrocket.orderservice.application;

import com.google.gson.Gson;
import com.teamrocket.orderservice.enums.OrderStatus;
import com.teamrocket.orderservice.model.dto.OrderCancelled;
import com.teamrocket.orderservice.model.dto.OrderDTO;
import com.teamrocket.orderservice.model.dto.OrderIdDTO;
import com.teamrocket.orderservice.model.dto.TaskVariables;
import com.teamrocket.orderservice.model.entity.CamundaOrderTask;
import com.teamrocket.orderservice.service.OrderService;
import com.teamrocket.orderservice.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaListener {
    @Value("${camunda.server.engine}")
    private String restEngine;


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private Gson GSON;

    public KafkaListener(OrderService orderService) {
        this.orderService = orderService;
    }

    public OrderDTO updateOrderStatus(int id, OrderStatus status) {
        OrderDTO result;
        try {
            result = orderService.updateOrderStatus(id, status);
            log.info("Order " + result.getId() + " changed status: " + status.toString());
            return result;
        } catch (Exception e) {
            log.error(e.getMessage());
            //TODO: Discuss whether order should be cancelled in this case or not
            return null;
        }
    }

    @org.springframework.kafka.annotation.KafkaListener(topics = "ORDER_ACCEPTED", groupId = "order-manager")
    public void orderAccepted(@Payload OrderIdDTO order) {
        updateOrderStatus(order.getSystemOrderId(), OrderStatus.IN_PROGRESS);
    }

    @org.springframework.kafka.annotation.KafkaListener(topics = "ORDER_READY", groupId = "order-manager")
    public void orderReady(@Payload OrderIdDTO order) {
        updateOrderStatus(order.getSystemOrderId(), OrderStatus.READY);
    }

    @org.springframework.kafka.annotation.KafkaListener(topics = "ORDER_PICKED_UP", groupId = "order-manager")
    public void orderPickedUp(@Payload OrderIdDTO order) {
        updateOrderStatus(order.getSystemOrderId(), OrderStatus.PICKED_UP);
    }

    @org.springframework.kafka.annotation.KafkaListener(topics = "ORDER_DELIVERED", groupId = "order-manager")
    public void orderDelivered(@Payload OrderIdDTO order) {
        updateOrderStatus(order.getSystemOrderId(), OrderStatus.COMPLETED);
        CamundaOrderTask task = taskService.getTaskById(order.getSystemOrderId());
        completeCamundaTask(task);
    }

    @org.springframework.kafka.annotation.KafkaListener(topics = "ORDER_CANCELED", groupId = "order-manager")
    @KafkaHandler
    public void orderCancelled(@Payload OrderCancelled orderCancelled) {

        OrderDTO result = updateOrderStatus(orderCancelled.getSystemOrderId(), OrderStatus.CANCELED);
        if (orderCancelled.getReason().equals("Unexpected error")) {
            cancelCamundaProcess(result.getProcessId());

        }
    }


    public void cancelCamundaProcess(String instanceId) {
        try {
            String endProcessURL = new StringBuilder(restEngine)
                    .append("process-instance/")
                    .append(instanceId)
                    .toString();
            restTemplate.delete(endProcessURL);
            log.info("Process instance " + instanceId + " was cancelled");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void completeCamundaTask(CamundaOrderTask task) {
        try {
            String url = new StringBuilder(restEngine)
                    .append("external-task/")
                    .append(task.getTaskId())
                    .append("/complete")
                    .toString();

            String requestBody = buildTaskVariables(task.getWorkerId());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            List<MediaType> mediaTypeList = new ArrayList();
            mediaTypeList.add(MediaType.APPLICATION_JSON);
            headers.setAccept(mediaTypeList);
            HttpEntity<String> request =
                    new HttpEntity<>(requestBody, headers);

            restTemplate.postForEntity(url, request, String.class);
            log.info("Completed order task with variables: {}", requestBody);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private String buildTaskVariables(String workerId) {
        Variables variables = new Variables();
        TaskVariables taskVariables = new TaskVariables(workerId, variables);
        return GSON.toJson(taskVariables, TaskVariables.class);
    }
}
