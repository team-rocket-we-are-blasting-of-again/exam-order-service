package com.teamrocket.Template.entity;

import com.teamrocket.Template.dto.OrderDTO;
import com.teamrocket.Template.dto.OrderItemDTO;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Order { 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "customerId", nullable = false)
    private Long customerId;

    @Column(name = "restaurantId", nullable = false)
    private Long restaurantId;

    @Column(name = "status", nullable = false)
    private String status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderItem> items = new ArrayList<>();

    public Order(Long customerId, Long restaurantId, String status, List<OrderItem> items) {
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.status = status;
        this.items = items;
    }

    public static Order fromDto(OrderDTO dto) {
        Order order = Order.builder()
                .customerId(dto.getCustomerId())
                .restaurantId(dto.getRestaurantId())
                .status(dto.getStatus())
                .items(OrderItem.fromList(dto.getItems()))
                .build();
        for (OrderItem item : order.getItems()) {
            item.setOrder(order);
        }
        return order;
    }

}
