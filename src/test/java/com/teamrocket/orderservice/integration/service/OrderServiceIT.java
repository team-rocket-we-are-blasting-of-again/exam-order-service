package com.teamrocket.orderservice.integration.service;

import com.teamrocket.orderservice.model.dto.OrderDTO;
import com.teamrocket.orderservice.model.dto.OrderItemDTO;
import com.teamrocket.orderservice.model.entity.Order;
import com.teamrocket.orderservice.enums.OrderStatus;
import com.teamrocket.orderservice.repository.OrderRepository;
import com.teamrocket.orderservice.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("integration")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class OrderServiceIT {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    void shouldSaveOrderInDB(){
        List<OrderItemDTO> orderItems = new ArrayList<>();
        orderItems.add(new OrderItemDTO("itemName2", 12.0, 2));
        OrderDTO order = new OrderDTO(2, 3, OrderStatus.READY, orderItems);
        OrderDTO result = orderService.saveOrder(order);
        assertNotNull(result);
    }

    @Test
     void shouldUpdateOrderStatus(){
        List<OrderItemDTO> orderItems = new ArrayList<>();
        orderItems.add(new OrderItemDTO("itemName", 10.0, 1));
        OrderDTO order = new OrderDTO(1, 2, OrderStatus.CANCELED, orderItems);
        Order savedOrder = orderRepository.save(Order.fromDto(order));
        int id = savedOrder.getId();
        OrderStatus status = OrderStatus.CANCELED;
        OrderDTO result = orderService.updateOrderStatus(id, OrderStatus.CANCELED);
        assertEquals(status, result.getStatus());
    }

    @Test
    void shouldGetOrderById(){
        List<OrderItemDTO> orderItems = new ArrayList<>();
        orderItems.add(new OrderItemDTO("itemName", 10.0, 1));
        OrderDTO order = new OrderDTO(1, 2, OrderStatus.CANCELED, orderItems);
        Order savedOrder = orderRepository.save(Order.fromDto(order));
        OrderDTO result = orderService.getOrderById(savedOrder.getId());
        assertEquals(order.getCustomerId(), result.getCustomerId());
        assertEquals(order.getRestaurantId(), result.getRestaurantId());
        assertEquals(order.getStatus(), result.getStatus());
    }

}
