package com.teamrocket.orderservice.repository;

import com.teamrocket.orderservice.model.entity.CamundaOrderTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<CamundaOrderTask, Integer> {
}
