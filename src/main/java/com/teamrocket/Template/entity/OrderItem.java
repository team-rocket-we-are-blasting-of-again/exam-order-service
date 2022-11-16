package com.teamrocket.Template.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "order_item")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "amount", nullable = false)
    private int amount;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}