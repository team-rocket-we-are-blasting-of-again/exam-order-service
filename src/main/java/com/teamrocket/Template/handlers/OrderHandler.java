package com.teamrocket.Template.handlers;

import com.teamrocket.Template.dto.OrderDTO;
import com.teamrocket.Template.repository.OrderRepository;
import com.teamrocket.Template.service.OrderServiceImpl;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ExternalTaskSubscription(topicName = "createOrder")
public class OrderHandler implements ExternalTaskHandler {
    @Autowired
    OrderRepository orderRepository;

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        // TODO:
        System.out.println();
        System.out.println();
        System.out.println("createOrder was run!");
        System.out.println();
        System.out.println();
        OrderDTO orderToCreate = externalTask.getVariable("order");
        OrderServiceImpl orderService = new OrderServiceImpl(orderRepository);
        OrderDTO orderCreated = orderService.saveOrder(orderToCreate);
        externalTaskService.complete(externalTask, externalTask.getAllVariables());
    }
}
