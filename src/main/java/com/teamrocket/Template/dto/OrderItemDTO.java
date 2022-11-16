package com.teamrocket.Template.dto;

import com.teamrocket.Template.entity.Order;
import com.teamrocket.Template.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
    private Long id;

    private String name;

    private double price;

    private int amount;

    private Long orderId;

    public OrderItemDTO(String name, double price, int amount) {
        this.name = name;
        this.price = price;
        this.amount = amount;
    }

    public OrderItemDTO(Long id, String name, double price, int amount) {
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
                    item.getName(), item.getPrice(),
                    item.getAmount(), item.getOrder().getId()));
        }
        return orderItems;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
