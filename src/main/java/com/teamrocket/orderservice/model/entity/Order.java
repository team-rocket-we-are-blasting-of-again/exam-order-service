package com.teamrocket.orderservice.model.entity;

import static java.util.Objects.nonNull;

import com.teamrocket.orderservice.enums.OrderStatus;
import com.teamrocket.orderservice.model.dto.OrderDTO;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "customerId")
    private Integer customerId;

    @Column(name = "legacyCustomerId")
    private Integer legacyCustomerId;

    @Column(name = "restaurantId")
    private Integer restaurantId;

    @Column(name = "legacyRestaurantId")
    private Integer legacyRestaurantId;

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
    private Integer courierId;

    @Column(name = "legacyCourierId")
    private Integer legacyCourierId;

    @Column(name = "processId")
    private String processId;

    public static Order fromDto(OrderDTO dto) {
        Order order = Order.builder()
            .customerId(dto.getCustomerId())
            .restaurantId(dto.getRestaurantId())
            .status(dto.getStatus())
            .withDelivery(dto.isWithDelivery())
            .items(OrderItem.fromList(dto.getItems()))
            .processId(dto.getProcessId())
            .createdAt(Timestamp.valueOf(LocalDateTime.now()))
            .build();
        for (OrderItem item : order.getItems()) {
            item.setOrder(order);
        }
        return order;
    }

    public Integer getCustomerId() {
        return nonNull(this.customerId) ?
            this.customerId :
            this.legacyCustomerId;
    }

    public Integer getRestaurantId() {
        return nonNull(this.restaurantId) ?
            this.restaurantId :
            this.legacyRestaurantId;
    }

    public Integer getCourierId() {
        return nonNull(this.courierId) ?
            this.courierId :
            this.legacyCourierId;
    }
}
