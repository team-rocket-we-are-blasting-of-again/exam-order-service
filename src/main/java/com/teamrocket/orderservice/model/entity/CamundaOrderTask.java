package com.teamrocket.orderservice.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "camundaOrderTasks")
public class CamundaOrderTask {
    private static final long serialVersionUID = 1L;

    @Id
    private int systemOrderId;
    private String processId;
    private String taskDefinitionKey;
    private String taskId;
    private String workerId;
}
