package microservice_traceability.traceability.domain.usecase;

import microservice_traceability.traceability.domain.model.OrderStatusHistory;
import microservice_traceability.traceability.domain.spi.IOrderStatusHistoryPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderStatusHistoryUseCasePortTest {

    @InjectMocks
    private OrderStatusHistoryUseCasePort orderStatusHistoryUseCasePort;

    @Mock
    private IOrderStatusHistoryPersistencePort orderStatusHistoryPersistencePort;

    private OrderStatusHistory orderStatusHistory;

    @BeforeEach
    void setUp() {
        orderStatusHistory = new OrderStatusHistory();
        orderStatusHistory.setOrderId(1L);
        orderStatusHistory.setClientId(2L);
        orderStatusHistory.setRestaurantId(3L);
        orderStatusHistory.setNewState("DELIVERED");
        orderStatusHistory.setStateChangeTime(LocalDateTime.now());
    }

    @Test
    void saveOrderStatusHistory_ShouldUseExistingCustomerMail_WhenCustomerMailIsNull() {

        String existingCustomerMail = "customer@example.com";
        when(orderStatusHistoryPersistencePort.findCustomerMailByOrderId(orderStatusHistory.getOrderId()))
                .thenReturn(existingCustomerMail);

        orderStatusHistoryUseCasePort.saveOrderStatusHistory(orderStatusHistory);

        assertEquals(existingCustomerMail, orderStatusHistory.getCustomerMail());
        verify(orderStatusHistoryPersistencePort, times(1))
                .findCustomerMailByOrderId(orderStatusHistory.getOrderId());
        verify(orderStatusHistoryPersistencePort, times(1))
                .saveOrderStatusHistory(orderStatusHistory);
    }

    @Test
    void saveOrderStatusHistory_ShouldNotChangeCustomerMail_WhenItIsAlreadyPresent() {

        orderStatusHistory.setCustomerMail("customer@provided.com");

        orderStatusHistoryUseCasePort.saveOrderStatusHistory(orderStatusHistory);

        verify(orderStatusHistoryPersistencePort, never()).findCustomerMailByOrderId(anyLong());
        verify(orderStatusHistoryPersistencePort, times(1))
                .saveOrderStatusHistory(orderStatusHistory);
    }

    @Test
    void saveOrderStatusHistory_ShouldSaveDirectly_WhenNoCustomerMailExists() {

        when(orderStatusHistoryPersistencePort.findCustomerMailByOrderId(orderStatusHistory.getOrderId()))
                .thenReturn(null);

        orderStatusHistoryUseCasePort.saveOrderStatusHistory(orderStatusHistory);

        verify(orderStatusHistoryPersistencePort, times(1))
                .findCustomerMailByOrderId(orderStatusHistory.getOrderId());
        verify(orderStatusHistoryPersistencePort, times(1))
                .saveOrderStatusHistory(orderStatusHistory);

        assertNull(orderStatusHistory.getCustomerMail());
    }
}