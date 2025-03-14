package microservice_traceability.traceability.application.handler;

import microservice_traceability.traceability.application.dto.EmployeeEfficiencyResponse;
import microservice_traceability.traceability.application.dto.OrderStatusHistoryRequest;
import microservice_traceability.traceability.application.dto.OrderStatusHistoryResponse;
import microservice_traceability.traceability.application.dto.OrderTimeResponse;
import microservice_traceability.traceability.application.mapper.IOrderStatusHistoryMapper;
import microservice_traceability.traceability.domain.api.IOrderStatusHistoryServicePort;
import microservice_traceability.traceability.domain.model.EmployeeEfficiency;
import microservice_traceability.traceability.domain.model.OrderStatusHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.Duration;
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

    @Test
    void getOrderTimeByOrderId_ShouldReturnOrderTimeResponse_WhenDurationIsCalculated() {
        Long orderId = 1L;
        Duration duration = Duration.ofHours(2).plusMinutes(15).plusSeconds(30);
        OrderTimeResponse expectedResponse = new OrderTimeResponse(orderId, 2, 15, 30);

        when(orderStatusHistoryService.calculateOrderDuration(orderId)).thenReturn(duration);
        when(orderStatusHistoryMapper.toOrderTimeResponse(orderId, duration)).thenReturn(expectedResponse);

        OrderTimeResponse result = orderStatusHistoryHandler.getOrderTimeByOrderId(orderId);

        assertNotNull(result);
        assertEquals(expectedResponse, result);

        verify(orderStatusHistoryService, times(1)).calculateOrderDuration(orderId);
        verify(orderStatusHistoryMapper, times(1)).toOrderTimeResponse(orderId, duration);
    }

    @Test
    void getEmployeeEfficiencyRanking_ShouldReturnEmployeeEfficiencyResponses_WhenEmployeesExist() {
        EmployeeEfficiency efficiency = EmployeeEfficiency.builder()
                .employeeId(100L)
                .duration(Duration.ofHours(1).plusMinutes(45).plusSeconds(10))
                .build();

        EmployeeEfficiencyResponse expectedResponse = new EmployeeEfficiencyResponse(100L, 1, 45, 10);

        when(orderStatusHistoryService.calculateAverageOrderCompletionTimePerEmployee())
                .thenReturn(List.of(efficiency));
        when(orderStatusHistoryMapper.toEmployeeEfficiencyResponse(efficiency))
                .thenReturn(expectedResponse);

        List<EmployeeEfficiencyResponse> result = orderStatusHistoryHandler.getEmployeeEfficiencyRanking();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedResponse, result.get(0));

        verify(orderStatusHistoryService, times(1)).calculateAverageOrderCompletionTimePerEmployee();
        verify(orderStatusHistoryMapper, times(1)).toEmployeeEfficiencyResponse(efficiency);
    }
}