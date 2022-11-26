package com.teamrocket.Template.unit.service;

import com.teamrocket.Template.dto.OrderDTO;
import com.teamrocket.Template.dto.OrderItemDTO;
import com.teamrocket.Template.entity.Order;
import com.teamrocket.Template.entity.OrderItem;
import com.teamrocket.Template.enums.OrderStatus;
import com.teamrocket.Template.repository.OrderRepository;
import com.teamrocket.Template.service.OrderService;
import com.teamrocket.Template.service.OrderServiceImpl;
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

    /*@Test
    void saveOrder() {
        List<OrderItemDTO> orderItems = new ArrayList<>();
        orderItems.add(new OrderItemDTO("itemName", 10.0, 1));
        OrderDTO order = new OrderDTO(1L, 2L, "status", orderItems);
        Mockito.when(repositoryMock.save(any())).thenReturn(Order.fromDto(order));

        OrderDTO retOrder = orderService.saveOrder(order);
        assertNotNull(retOrder);
        Mockito.verify(repositoryMock).save(any());
        Mockito.verifyNoMoreInteractions(repositoryMock);
    }*/

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