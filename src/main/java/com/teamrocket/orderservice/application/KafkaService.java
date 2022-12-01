package com.teamrocket.orderservice.application;

import com.google.gson.Gson;
import com.teamrocket.orderservice.model.dto.OrderCancelled;
import com.teamrocket.orderservice.model.dto.OrderDTO;
import com.teamrocket.orderservice.enums.OrderStatus;
import com.teamrocket.orderservice.model.dto.OrderIdDTO;
import com.teamrocket.orderservice.model.dto.TaskVariables;
import com.teamrocket.orderservice.model.entity.CamundaOrderTask;
import com.teamrocket.orderservice.repository.TaskRepository;
import com.teamrocket.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaService {
    @Value("${camunda.server.engine}")
    private String restEngine;

    private RestTemplate restTemplate;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TaskRepository taskRepository;

    private Gson GSON = new Gson();

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
    public void orderAccepted(@Payload OrderIdDTO order) {
        updateOrderStatus(order.getSystemOrderId(), OrderStatus.IN_PROGRESS);
    }

    @KafkaListener(topics = "ORDER_READY", groupId = "order-manager")
    public void orderReady(@Payload OrderIdDTO order) {
        updateOrderStatus(order.getSystemOrderId(), OrderStatus.READY);
    }

    @KafkaListener(topics = "ORDER_PICKED_UP", groupId = "order-manager")
    public void orderPickedUp(@Payload OrderIdDTO order) {
        updateOrderStatus(order.getSystemOrderId(), OrderStatus.PICKED_UP);
    }

    @KafkaListener(topics = "ORDER_DELIVERED", groupId = "order-manager")
    public void orderDelivered(@Payload OrderIdDTO order) {
        updateOrderStatus(order.getSystemOrderId(), OrderStatus.COMPLETED);
        CamundaOrderTask task = taskRepository.getById(order.getSystemOrderId());
        completeCamundaTask(task);
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

    public void completeCamundaTask(CamundaOrderTask task) {
        restTemplate = new RestTemplate();
        try {
            String url = new StringBuilder(restEngine)
                    .append("external-task/")
                    .append(task.getTaskId())
                    .append("/complete")
                    .toString();

            String requestBody = buildTaskVariables(task.getWorkerId());

            log.info("FIRE TASK URL: {}", url);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            List<MediaType> mediaTypeList = new ArrayList();
            mediaTypeList.add(MediaType.APPLICATION_JSON);
            headers.setAccept(mediaTypeList);
            HttpEntity<String> request =
                    new HttpEntity<>(requestBody, headers);

            restTemplate.postForEntity(url, request, String.class);
            log.info("completeCamundaTask with variables: {}", requestBody);

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
