package com.teamrocket.orderservice.service;

import com.teamrocket.orderservice.model.entity.CamundaOrderTask;
import com.teamrocket.orderservice.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public CamundaOrderTask createTask(CamundaOrderTask task) {
        CamundaOrderTask storedTask = taskRepository.save(task);
        return storedTask;
    }

    public CamundaOrderTask getTaskById(int systemOrderId) {
        CamundaOrderTask storedTask = taskRepository.getById(systemOrderId);
        return storedTask;
    }
}
