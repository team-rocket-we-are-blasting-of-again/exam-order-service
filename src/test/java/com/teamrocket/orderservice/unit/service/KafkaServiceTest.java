package com.teamrocket.orderservice.unit.service;

import com.teamrocket.orderservice.application.KafkaService;
import com.teamrocket.orderservice.enums.OrderStatus;
import com.teamrocket.orderservice.model.dto.*;
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
        OrderIdDTO idDTO = new OrderIdDTO(1);

        kafkaService.orderAccepted(idDTO);
        Mockito.verify(orderServiceMock).updateOrderStatus(idDTO.getSystemOrderId(), OrderStatus.IN_PROGRESS);
        Mockito.verifyNoMoreInteractions(orderServiceMock);
    }

    @Test
    void shouldChangeStatusToREADY() {
        OrderIdDTO idDTO = new OrderIdDTO(1);

        kafkaService.orderReady(idDTO);
        Mockito.verify(orderServiceMock).updateOrderStatus(idDTO.getSystemOrderId(), OrderStatus.READY);
        Mockito.verifyNoMoreInteractions(orderServiceMock);
    }

    @Test
    void shouldChangeStatusToPICKED_UP() {
        OrderIdDTO idDTO = new OrderIdDTO(1);

        kafkaService.orderPickedUp(idDTO);
        Mockito.verify(orderServiceMock).updateOrderStatus(idDTO.getSystemOrderId(), OrderStatus.PICKED_UP);
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