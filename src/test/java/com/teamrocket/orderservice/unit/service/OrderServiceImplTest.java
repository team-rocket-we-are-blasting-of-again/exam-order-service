package com.teamrocket.orderservice.unit.service;

import com.teamrocket.orderservice.model.dto.OrderDTO;
import com.teamrocket.orderservice.model.dto.OrderItemDTO;
import com.teamrocket.orderservice.model.entity.Order;
import com.teamrocket.orderservice.enums.OrderStatus;
import com.teamrocket.orderservice.repository.OrderRepository;
import com.teamrocket.orderservice.service.OrderService;
import com.teamrocket.orderservice.service.OrderServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("unit")
class OrderServiceImplTest {

    private OrderService orderService;
    private OrderRepository repositoryMock;

    @BeforeEach
    public void setup() {
        repositoryMock = mock(OrderRepository.class);
        orderService = new OrderServiceImpl(repositoryMock);
    }

    @Test
    void saveOrder() {
        List<OrderItemDTO> orderItems = new ArrayList<>();
        orderItems.add(new OrderItemDTO("itemName", 10.0, 1));
        OrderDTO order = new OrderDTO(1, 2, OrderStatus.PENDING, orderItems);
        Mockito.when(repositoryMock.save(any())).thenReturn(Order.fromDto(order));

        OrderDTO retOrder = orderService.saveOrder(order);
        assertNotNull(retOrder);
        Mockito.verify(repositoryMock).save(any());
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void updateOrderStatus() {
        int orderId = 1;
        OrderStatus status = OrderStatus.READY;
        List<OrderItemDTO> orderItems = new ArrayList<>();
        orderItems.add(new OrderItemDTO("itemName", 10.0, 1));
        OrderDTO order = new OrderDTO(1, 2, OrderStatus.READY, orderItems);
        Mockito.when(repositoryMock.findById(orderId)).thenReturn(Optional.ofNullable(Order.fromDto(order)));

        OrderDTO retOrder = orderService.updateOrderStatus(orderId, status);
        assertNotNull(retOrder);
        Mockito.verify(repositoryMock).findById(orderId);
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    void getOrderById() {
        int orderId = 1;
        List<OrderItemDTO> orderItems = new ArrayList<>();
        orderItems.add(new OrderItemDTO("itemName", 10.0, 1));
        OrderDTO order = new OrderDTO(1, 2, OrderStatus.CANCELED, orderItems);
        Mockito.when(repositoryMock.findById(orderId)).thenReturn(Optional.ofNullable(Order.fromDto(order)));

        OrderDTO retOrder = orderService.getOrderById(orderId);
        assertNotNull(retOrder);
        Mockito.verify(repositoryMock).findById(orderId);
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }
}