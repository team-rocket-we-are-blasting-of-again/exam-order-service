package com.teamrocket.orderservice.model.dto;

import com.teamrocket.orderservice.model.entity.Order;
import com.teamrocket.orderservice.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderDTO {
    private int id;
    private int customerId;
    private int restaurantId;
    private OrderStatus status;
    private List<OrderItemDTO> items;
    private Timestamp createdAt;
    private Timestamp deliveryTime;
    private boolean withDelivery;
    private String processId;

    public OrderDTO(int customerId, int restaurantId, OrderStatus status, List<OrderItemDTO> items) {
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.status = status;
        this.items = items;
    }
    public OrderDTO(int id, int customerId, int restaurantId, OrderStatus status, Timestamp createdAt, List<OrderItemDTO> items, boolean withDelivery, String processId) {
        this.id = id;
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.status = status;
        this.createdAt = createdAt;
        this.items = items;
        this.withDelivery = withDelivery;
        this.processId = processId;
    }

    public OrderDTO(NewOrderDTO dto) {
        this.customerId = dto.getCustomerId().intValue();
        this.restaurantId = dto.getRestaurantId().intValue();
        this.status = OrderStatus.PENDING;
        this.withDelivery = dto.isWithDelivery();
        this.items = OrderItemDTO.fromNewItems(dto.getItems());
    }

    public static OrderDTO fromOrder(Order order) {
        return new OrderDTO(order.getId(),
                order.getCustomerId(),
                order.getRestaurantId(),
                order.getStatus(),
                order.getCreatedAt(),
                OrderItemDTO.fromList(order.getItems()),
                order.isWithDelivery(),
        order.getProcessId());
    }

}
