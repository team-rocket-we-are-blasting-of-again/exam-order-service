package com.teamrocket.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewOrderDTO {
    private Long customerId;
    private Long restaurantId;
    private Date dateCreated;
    private boolean withDelivery;
    private List<NewOrderItem> items;
}
