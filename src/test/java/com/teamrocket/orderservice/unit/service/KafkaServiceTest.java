package com.teamrocket.orderservice.unit.service;

import com.teamrocket.orderservice.application.KafkaListener;
import com.teamrocket.orderservice.enums.OrderStatus;
import com.teamrocket.orderservice.model.dto.*;
import com.teamrocket.orderservice.service.OrderService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("unit")
class KafkaServiceTest {

    private KafkaListener kafkaListener;
    private OrderService orderServiceMock;
    private RestTemplate restTemplateMock;

    @BeforeEach
    void setUp() {
        orderServiceMock = mock(OrderService.class);
        restTemplateMock = mock(RestTemplate.class);
        kafkaListener = new KafkaListener(orderServiceMock);
    }

    @AfterEach
    void tearDown() {
        orderServiceMock = null;
        kafkaListener = null;
    }

    @Test
    void shouldChangeStatusToIN_PROGRESS() {
        OrderIdDTO idDTO = new OrderIdDTO(1);

        kafkaListener.orderAccepted(idDTO);
        Mockito.verify(orderServiceMock).updateOrderStatus(idDTO.getSystemOrderId(), OrderStatus.IN_PROGRESS);
        Mockito.verifyNoMoreInteractions(orderServiceMock);
    }

    @Test
    void shouldChangeStatusToREADY() {
        OrderIdDTO idDTO = new OrderIdDTO(1);

        kafkaListener.orderReady(idDTO);
        Mockito.verify(orderServiceMock).updateOrderStatus(idDTO.getSystemOrderId(), OrderStatus.READY);
        Mockito.verifyNoMoreInteractions(orderServiceMock);
    }

    @Test
    void shouldChangeStatusToPICKED_UP() {
        OrderIdDTO idDTO = new OrderIdDTO(1);

        kafkaListener.orderPickedUp(idDTO);
        Mockito.verify(orderServiceMock).updateOrderStatus(idDTO.getSystemOrderId(), OrderStatus.PICKED_UP);
        Mockito.verifyNoMoreInteractions(orderServiceMock);
    }

    @Test
    void shouldChangeStatusToCANCELED() {
        OrderCancelled order = new OrderCancelled(1, "an error occured");
        OrderDTO dto = new OrderDTO(1, 1, OrderStatus.CANCELED, new ArrayList<>());
        Mockito.when(orderServiceMock.updateOrderStatus(1, OrderStatus.CANCELED)).thenReturn(dto);

        kafkaListener.orderCancelled(order);
        Mockito.verify(orderServiceMock).updateOrderStatus(order.getSystemOrderId(), OrderStatus.CANCELED);
        Mockito.verifyNoMoreInteractions(orderServiceMock);
    }
}