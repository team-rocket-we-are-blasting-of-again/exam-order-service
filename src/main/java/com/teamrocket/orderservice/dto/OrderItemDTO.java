package com.teamrocket.orderservice.dto;

import com.teamrocket.orderservice.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderItemDTO {
    private int id;

    private int menuItemId;

    private String name;

    private double price;

    private int amount;

    private int orderId;

    public OrderItemDTO(String name, double price, int amount) {
        this.name = name;
        this.price = price;
        this.amount = amount;
    }

    public OrderItemDTO(int id, String name, double price, int amount) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.amount = amount;
    }
    public static List<OrderItemDTO> fromList(List<OrderItem> items) {
        List<OrderItemDTO> orderItems = new ArrayList<>();
        for (OrderItem item : items
             ) {
            orderItems.add(new OrderItemDTO(item.getId(),
                    item.getMenuItemId(),
                    item.getName(), item.getPrice(),
                    item.getAmount(), item.getOrder().getId()));
        }
        return orderItems;
    }

    public static List<OrderItemDTO> fromNewItems(List<NewOrderItem> items) {
        List<OrderItemDTO> orderItems = new ArrayList<>();
        for (NewOrderItem item : items
             ) {
            OrderItemDTO dto = new OrderItemDTO();
            dto.setMenuItemId(item.getMenuItemId());
            dto.setAmount(item.getQuantity());
            orderItems.add(dto);
        }
        return orderItems;
    }
}
