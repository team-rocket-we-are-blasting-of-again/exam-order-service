package com.teamrocket.orderservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.camunda.bpm.engine.variable.Variables;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskVariables {

    String workerId;
    Variables variables;
}
