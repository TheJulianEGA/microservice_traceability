package microservice_traceability.traceability.application.handler;

import microservice_traceability.traceability.application.dto.OrderStatusHistoryRequest;
import microservice_traceability.traceability.application.dto.OrderStatusHistoryResponse;
import microservice_traceability.traceability.application.mapper.IOrderStatusHistoryMapper;
import microservice_traceability.traceability.domain.api.IOrderStatusHistoryServicePort;
import microservice_traceability.traceability.domain.model.OrderStatusHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderStatusHistoryHandlerTest {

    @InjectMocks
    private OrderStatusHistoryHandler orderStatusHistoryHandler;

    @Mock
    private IOrderStatusHistoryMapper orderStatusHistoryMapper;

    @Mock
    private IOrderStatusHistoryServicePort orderStatusHistoryService;

    private OrderStatusHistoryRequest request;
    private OrderStatusHistory orderStatusHistory;
    private OrderStatusHistoryResponse orderStatusHistoryResponse;

    @BeforeEach
    void setUp() {
        request = new OrderStatusHistoryRequest();

        orderStatusHistory = new OrderStatusHistory();

        orderStatusHistoryResponse = new OrderStatusHistoryResponse();
    }

    @Test
    void saveOrderStatusHistory_ShouldCallService_WhenRequestIsValid() {
        when(orderStatusHistoryMapper.toModel(request)).thenReturn(orderStatusHistory);

        orderStatusHistoryHandler.saveOrderStatusHistory(request);

        verify(orderStatusHistoryMapper, times(1)).toModel(request);
        verify(orderStatusHistoryService, times(1)).saveOrderStatusHistory(orderStatusHistory);
    }

    @Test
    void getOrderStatusHistoryByOrderId_ShouldReturnResponseList_WhenRecordsExist() {
        Long orderId = 1L;
        List<OrderStatusHistory> historyList = List.of(orderStatusHistory);
        List<OrderStatusHistoryResponse> expectedResponseList = List.of(orderStatusHistoryResponse);

        when(orderStatusHistoryService.getOrderStatusHistoryByOrderId(orderId)).thenReturn(historyList);
        when(orderStatusHistoryMapper.toResponse(orderStatusHistory)).thenReturn(orderStatusHistoryResponse);

        List<OrderStatusHistoryResponse> result = orderStatusHistoryHandler.getOrderStatusHistoryByOrderId(orderId);

        assertNotNull(result);
        assertEquals(expectedResponseList.size(), result.size());

        verify(orderStatusHistoryService, times(1)).getOrderStatusHistoryByOrderId(orderId);
        verify(orderStatusHistoryMapper, times(1)).toResponse(orderStatusHistory);
    }

    @Test
    void getOrderStatusHistoryByOrderId_ShouldReturnEmptyList_WhenNoRecordsExist() {
        Long orderId = 1L;

        when(orderStatusHistoryService.getOrderStatusHistoryByOrderId(orderId)).thenReturn(Collections.emptyList());

        List<OrderStatusHistoryResponse> result = orderStatusHistoryHandler.getOrderStatusHistoryByOrderId(orderId);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(orderStatusHistoryService, times(1)).getOrderStatusHistoryByOrderId(orderId);
        verify(orderStatusHistoryMapper, never()).toResponse(any());
    }
}