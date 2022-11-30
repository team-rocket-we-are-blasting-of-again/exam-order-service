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
public class NewOrder {
    private int id;
    private int restaurantId;
    private int customerId;
    private Date createdAt;
    private OrderStatus status;
    private boolean withDelivery;
    private double totalPrice;
    private List<NewOrderItem> items;

    public NewOrder(OrderDTO order) {
        this.id = order.getId();
        this.restaurantId = order.getRestaurantId();
        this.customerId = order.getCustomerId();
        this.createdAt = order.getCreatedAt();
        this.status = order.getStatus();
        this.withDelivery = order.isWithDelivery();
        this.items = NewOrderItem.fromItemDTOList(order.getItems());
    }
}
