package com.teamrocket.Template.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamrocket.Template.dto.NewOrderDTO;
import com.teamrocket.Template.dto.OrderDTO;
import com.teamrocket.Template.repository.OrderRepository;
import com.teamrocket.Template.service.OrderServiceImpl;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ExternalTaskSubscription(topicName = "createOrder")
public class OrderHandler implements ExternalTaskHandler {
    @Autowired
    OrderRepository orderRepository;

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        // TODO: Make sure to update with new model
        Gson gson = new GsonBuilder().create();
        NewOrderDTO orderToCreate = gson.fromJson(externalTask.getVariableTyped("order").getValue().toString(), NewOrderDTO.class);
        OrderServiceImpl orderService = new OrderServiceImpl(orderRepository);
        OrderDTO orderCreated = orderService.saveOrder(new OrderDTO(orderToCreate));
        Map<String, Object> allVariables = externalTask.getAllVariables();
        allVariables.put("order", gson.toJson(orderCreated));
        externalTaskService.complete(externalTask, allVariables);
    }
}
