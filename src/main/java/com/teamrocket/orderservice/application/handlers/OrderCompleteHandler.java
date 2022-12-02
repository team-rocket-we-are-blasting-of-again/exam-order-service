package com.teamrocket.orderservice.application.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamrocket.orderservice.application.KafkaService;
import com.teamrocket.orderservice.model.dto.OrderDTO;
import com.teamrocket.orderservice.model.entity.CamundaOrderTask;
import com.teamrocket.orderservice.repository.TaskRepository;
import com.teamrocket.orderservice.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ExternalTaskSubscription(topicName = "completeOrder")
@Slf4j
public class OrderCompleteHandler implements ExternalTaskHandler {

    @Autowired
    TaskService taskService;

    @Autowired
    KafkaService kafkaService;

    @Autowired
    Gson gson;

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        String processId = externalTask.getProcessInstanceId();
        String taskDefinitionKey = externalTask.getActivityId();
        String taskId = externalTask.getId();
        String workerId = externalTask.getWorkerId();

        int systemOrderId = gson.fromJson(externalTask.getVariableTyped("systemOrderId").getValue().toString(), Integer.class);
        CamundaOrderTask task = new CamundaOrderTask(systemOrderId, processId, taskDefinitionKey, taskId, workerId);
        taskService.createTask(task);
    }
}
