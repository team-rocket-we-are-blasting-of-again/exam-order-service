package com.teamrocket.Template.entity;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "customerId", nullable = false)
    private Long customerId;

    @Column(name = "restaurantId", nullable = false)
    private Long restaurantId;

    @Column(name = "status", nullable = false)
    private String status;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> items = new ArrayList<>();
}
