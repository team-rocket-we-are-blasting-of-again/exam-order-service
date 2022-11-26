package com.teamrocket.Template.entity;

import com.teamrocket.Template.dto.OrderDTO;
import com.teamrocket.Template.dto.OrderItemDTO;
import com.teamrocket.Template.enums.OrderStatus;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Order { 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "customerId", nullable = false)
    private int customerId;

    @Column(name = "restaurantId", nullable = false)
    private int restaurantId;

    @Column(name = "createdAt")
    private Timestamp createdAt;

    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderItem> items = new ArrayList<>();

    @Column(name = "orderPrice")
    private double orderPrice;

    @Column(name = "deliveryPrice")
    private double deliveryPrice;

    @Column(name = "withDelivery")
    private boolean withDelivery;

    @Column(name = "courierId")
    private int courierId;

    public Order(int id, int customerId, int restaurantId, Timestamp createdAt, OrderStatus status, List<OrderItem> items, double orderPrice, double deliveryPrice, boolean withDelivery, int courierId) {
        this.id = id;
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.createdAt = Timestamp.valueOf(LocalDateTime.now());
        this.status = status;
        this.items = items;
        this.orderPrice = orderPrice;
        this.deliveryPrice = deliveryPrice;
        this.withDelivery = withDelivery;
        this.courierId = courierId;
    }

    public static Order fromDto(OrderDTO dto) {
        Order order = Order.builder()
                .customerId(dto.getCustomerId())
                .restaurantId(dto.getRestaurantId())
                .status(dto.getStatus())
                .withDelivery(dto.isWithDelivery())
                .items(OrderItem.fromList(dto.getItems()))
                .build();
        for (OrderItem item : order.getItems()) {
            item.setOrder(order);
        }
        return order;
    }

}
