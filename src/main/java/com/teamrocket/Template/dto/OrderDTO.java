package com.teamrocket.Template.dto;

import com.teamrocket.Template.entity.Order;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private Long customerId;
    private Long restaurantId;
    private String status;
    private List<OrderItemDTO> items;

    public OrderDTO(Long customerId, Long restaurantId, String status, List<OrderItemDTO> items) {
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.status = status;
        this.items = items;
    }

    public static OrderDTO fromOrder(Order order) {
        return new OrderDTO(order.getId(),
                order.getCustomerId(), order.getRestaurantId(),
                order.getStatus(), OrderItemDTO.fromList(order.getItems()));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }
}
