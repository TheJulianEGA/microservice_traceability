package microservice_traceability.traceability.domain.usecase;

import microservice_traceability.traceability.domain.exception.OrderAccessForbiddenException;
import microservice_traceability.traceability.domain.exception.OrderStatusHistoryNotFoundException;
import microservice_traceability.traceability.domain.model.OrderStatusHistory;
import microservice_traceability.traceability.domain.security.IAuthenticationSecurityPort;
import microservice_traceability.traceability.domain.spi.IOrderStatusHistoryPersistencePort;
import microservice_traceability.traceability.domain.util.DomainConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderStatusHistoryUseCasePortTest {

    @InjectMocks
    private OrderStatusHistoryUseCasePort orderStatusHistoryUseCasePort;

    @Mock
    private IOrderStatusHistoryPersistencePort orderStatusHistoryPersistencePort;

    @Mock
    private IAuthenticationSecurityPort authenticationSecurityPort;

    private OrderStatusHistory orderStatusHistory;
    private List<OrderStatusHistory> orderStatusHistoryList;

    @BeforeEach
    void setUp() {
        orderStatusHistory = new OrderStatusHistory();
        orderStatusHistory.setOrderId(1L);
        orderStatusHistory.setClientId(2L);
        orderStatusHistory.setRestaurantId(3L);
        orderStatusHistory.setNewState("DELIVERED");
        orderStatusHistory.setStateChangeTime(LocalDateTime.now());

        orderStatusHistoryList = List.of(orderStatusHistory);
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

    @Test
    void getOrderStatusHistoryByOrderId_ShouldThrowException_WhenHistoryDoesNotExist() {
        when(orderStatusHistoryPersistencePort.findByOrderId(1L)).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(OrderStatusHistoryNotFoundException.class, () ->
                orderStatusHistoryUseCasePort.getOrderStatusHistoryByOrderId(1L));

        assertEquals(DomainConstants.NO_ORDER_HISTORY_FOUND, exception.getMessage());
        verify(orderStatusHistoryPersistencePort, times(1)).findByOrderId(1L);
    }

    @Test
    void getOrderStatusHistoryByOrderId_ShouldThrowException_WhenClientHasNoAccess() {
        when(orderStatusHistoryPersistencePort.findByOrderId(1L)).thenReturn(orderStatusHistoryList);
        when(authenticationSecurityPort.getAuthenticatedUserId()).thenReturn(99L);

        Exception exception = assertThrows(OrderAccessForbiddenException.class, () ->
                orderStatusHistoryUseCasePort.getOrderStatusHistoryByOrderId(1L));

        assertEquals(DomainConstants.ACCESS_FORBIDDEN, exception.getMessage());
        verify(orderStatusHistoryPersistencePort, times(1)).findByOrderId(1L);
        verify(authenticationSecurityPort, times(1)).getAuthenticatedUserId();
    }

    @Test
    void getOrderStatusHistoryByOrderId_ShouldReturnHistoryList_WhenValidRequest() {
        when(orderStatusHistoryPersistencePort.findByOrderId(1L)).thenReturn(orderStatusHistoryList);
        when(authenticationSecurityPort.getAuthenticatedUserId()).thenReturn(2L);

        List<OrderStatusHistory> result = orderStatusHistoryUseCasePort.getOrderStatusHistoryByOrderId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(orderStatusHistory.getOrderId(), result.get(0).getOrderId());

        verify(orderStatusHistoryPersistencePort, times(1)).findByOrderId(1L);
        verify(authenticationSecurityPort, times(1)).getAuthenticatedUserId();
    }
}