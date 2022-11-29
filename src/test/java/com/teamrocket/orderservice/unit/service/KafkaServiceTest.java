package com.teamrocket.orderservice.unit.service;

import com.teamrocket.orderservice.application.KafkaService;
import com.teamrocket.orderservice.enums.OrderStatus;
import com.teamrocket.orderservice.model.dto.NewOrderItem;
import com.teamrocket.orderservice.model.dto.OrderCancelled;
import com.teamrocket.orderservice.model.dto.OrderDTO;
import com.teamrocket.orderservice.model.dto.RestaurantOrder;
import com.teamrocket.orderservice.service.OrderService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.mock;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("unit")
class KafkaServiceTest {

    private KafkaService kafkaService;
    private OrderService orderServiceMock;
    private RestTemplate restTemplateMock;

    @BeforeEach
    void setUp() {
        orderServiceMock = mock(OrderService.class);
        restTemplateMock = mock(RestTemplate.class);
        kafkaService = new KafkaService(orderServiceMock);
    }

    @AfterEach
    void tearDown() {
        orderServiceMock = null;
        kafkaService = null;
    }

    @Test
    void shouldChangeStatusToIN_PROGRESS() {
        List<NewOrderItem> items = new ArrayList<>();
        items.add(new NewOrderItem(1, 2));
        RestaurantOrder order = new RestaurantOrder(1, 1, new Date(),
                OrderStatus.PENDING, true, 10.0, items);

        kafkaService.orderAccepted(order);
        Mockito.verify(orderServiceMock).updateOrderStatus(order.getId(), OrderStatus.IN_PROGRESS);
        Mockito.verifyNoMoreInteractions(orderServiceMock);
    }

    @Test
    void shouldChangeStatusToREADY() {
        List<NewOrderItem> items = new ArrayList<>();
        items.add(new NewOrderItem(1, 2));
        RestaurantOrder order = new RestaurantOrder(1, 1, new Date(),
                OrderStatus.PENDING, true, 10.0, items);

        kafkaService.orderReady(order);
        Mockito.verify(orderServiceMock).updateOrderStatus(order.getId(), OrderStatus.READY);
        Mockito.verifyNoMoreInteractions(orderServiceMock);
    }

    @Test
    void shouldChangeStatusToPICKED_UP() {
        List<NewOrderItem> items = new ArrayList<>();
        items.add(new NewOrderItem(1, 2));
        RestaurantOrder order = new RestaurantOrder(1, 1, new Date(),
                OrderStatus.PENDING, true, 10.0, items);

        kafkaService.orderPickedUp(order);
        Mockito.verify(orderServiceMock).updateOrderStatus(order.getId(), OrderStatus.PICKED_UP);
        Mockito.verifyNoMoreInteractions(orderServiceMock);
    }

    @Test
    void shouldChangeStatusToCANCELED() {
        OrderCancelled order = new OrderCancelled(1, "an error occured");
        OrderDTO dto = new OrderDTO(1, 1, OrderStatus.CANCELED, new ArrayList<>());
        Mockito.when(orderServiceMock.updateOrderStatus(1, OrderStatus.CANCELED)).thenReturn(dto);

        kafkaService.orderCancelled(order);
        Mockito.verify(orderServiceMock).updateOrderStatus(order.getSystemOrderId(), OrderStatus.CANCELED);
        Mockito.verifyNoMoreInteractions(orderServiceMock);
    }
}