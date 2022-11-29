package com.teamrocket.orderservice.model.entity;

import com.teamrocket.orderservice.model.dto.OrderDTO;
import com.teamrocket.orderservice.enums.OrderStatus;
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
    @Enumerated(EnumType.STRING)
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

    @Column(name = "processId")
    private String processId;

    public Order(int id, int customerId, int restaurantId, Timestamp createdAt, OrderStatus status, List<OrderItem> items, double orderPrice, double deliveryPrice, boolean withDelivery, int courierId, String processId) {
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
        this.processId = processId;
    }

    public static Order fromDto(OrderDTO dto) {
        Order order = Order.builder()
                .customerId(dto.getCustomerId())
                .restaurantId(dto.getRestaurantId())
                .status(dto.getStatus())
                .withDelivery(dto.isWithDelivery())
                .items(OrderItem.fromList(dto.getItems()))
                .processId(dto.getProcessId())
                .build();
        for (OrderItem item : order.getItems()) {
            item.setOrder(order);
        }
        return order;
    }

}
