package com.teamrocket.Template.entity;

import com.teamrocket.Template.dto.OrderItemDTO;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order_item")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "menuItemId")
    private int menuItemId;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private double price;

    @Column(name = "amount")
    private int amount;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public OrderItem(String name, double price, int amount) {
        this.name = name;
        this.price = price;
        this.amount = amount;
    }
    public OrderItem(String name, double price, int amount, Order order) {
        this.order = order;
        this.name = name;
        this.price = price;
        this.amount = amount;
    }

    public static List<OrderItem> fromList(List<OrderItemDTO> items) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemDTO item : items
        ) {
            orderItems.add(new OrderItem(item.getName(), item.getPrice(), item.getAmount()));
        }
        return orderItems;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", amount=" + amount +
                ", order=" + order +
                '}';
    }
}