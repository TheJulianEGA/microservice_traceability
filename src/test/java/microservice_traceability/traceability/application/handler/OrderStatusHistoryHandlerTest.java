package microservice_traceability.traceability.application.handler;

import microservice_traceability.traceability.application.dto.OrderStatusHistoryRequest;
import microservice_traceability.traceability.application.mapper.IOrderStatusHistoryMapper;
import microservice_traceability.traceability.domain.api.IOrderStatusHistoryServicePort;
import microservice_traceability.traceability.domain.model.OrderStatusHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


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

    @BeforeEach
    void setUp() {
        request = new OrderStatusHistoryRequest();

        orderStatusHistory = new OrderStatusHistory();

    }

    @Test
    void saveOrderStatusHistory_ShouldCallService_WhenRequestIsValid() {

        when(orderStatusHistoryMapper.toModel(request)).thenReturn(orderStatusHistory);

        orderStatusHistoryHandler.saveOrderStatusHistory(request);

        verify(orderStatusHistoryMapper, times(1)).toModel(request);
        verify(orderStatusHistoryService, times(1)).saveOrderStatusHistory(orderStatusHistory);
    }
}