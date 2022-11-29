package com.teamrocket.orderservice.model.dto;

import com.teamrocket.orderservice.enums.OrderStatus;
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
public class RestaurantOrder {
    private int id;
    private int restaurantId;
    private Date createdAt;
    private OrderStatus status;
    private boolean withDelivery;
    private double totalPrice;
    private List<NewOrderItem> items;

}
