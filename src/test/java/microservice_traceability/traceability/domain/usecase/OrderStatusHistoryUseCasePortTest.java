package microservice_traceability.traceability.domain.usecase;

import microservice_traceability.traceability.domain.exception.OrderAccessForbiddenException;
import microservice_traceability.traceability.domain.exception.OrderStatusHistoryNotFoundException;
import microservice_traceability.traceability.domain.model.EmployeeEfficiency;
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

import java.time.Duration;
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
    void saveOrderStatusHistory_ShouldRetrieveAndUseExistingCustomerMail_WhenNewEntryHasNullMail() {
        String existingCustomerMail = "customer@example.com";

        OrderStatusHistory existingOrderStatus = new OrderStatusHistory();
        existingOrderStatus.setCustomerMail(existingCustomerMail);

        when(orderStatusHistoryPersistencePort.findByOrderId(orderStatusHistory.getOrderId()))
                .thenReturn(List.of(existingOrderStatus));

        orderStatusHistoryUseCasePort.saveOrderStatusHistory(orderStatusHistory);

        assertEquals(existingCustomerMail, orderStatusHistory.getCustomerMail());
        verify(orderStatusHistoryPersistencePort, times(1))
                .findByOrderId(orderStatusHistory.getOrderId());
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
        when(orderStatusHistoryPersistencePort.findByOrderId(orderStatusHistory.getOrderId()))
                .thenReturn(Collections.emptyList());

        orderStatusHistoryUseCasePort.saveOrderStatusHistory(orderStatusHistory);

        verify(orderStatusHistoryPersistencePort, times(1))
                .findByOrderId(orderStatusHistory.getOrderId());
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

    @Test
    void calculateOrderDuration_ShouldReturnCorrectDuration_WhenOrderExists() {
        Long orderId = 1L;
        Long ownerId = 2L;
        LocalDateTime startTime = LocalDateTime.now().minusHours(2);
        LocalDateTime endTime = LocalDateTime.now();

        OrderStatusHistory pendingStatus = new OrderStatusHistory();
        pendingStatus.setNewState(DomainConstants.STATUS_PENDING);
        pendingStatus.setStateChangeTime(startTime);
        pendingStatus.setOwnerId(ownerId);

        OrderStatusHistory deliveredStatus = new OrderStatusHistory();
        deliveredStatus.setNewState(DomainConstants.STATUS_DELIVERY);
        deliveredStatus.setStateChangeTime(endTime);
        deliveredStatus.setOwnerId(ownerId);

        List<OrderStatusHistory> historyList = List.of(pendingStatus, deliveredStatus);

        when(orderStatusHistoryPersistencePort.findByOrderId(orderId)).thenReturn(historyList);
        when(authenticationSecurityPort.getAuthenticatedUserId()).thenReturn(ownerId);
        Duration result = orderStatusHistoryUseCasePort.calculateOrderDuration(orderId);

        assertEquals(Duration.between(startTime, endTime), result);

        verify(orderStatusHistoryPersistencePort, times(1)).findByOrderId(orderId);
        verify(authenticationSecurityPort, times(1)).getAuthenticatedUserId();
    }

    @Test
    void calculateOrderDuration_ShouldThrowException_WhenPendingStatusNotFound() {
        Long orderId = 1L;
        Long ownerId = 2L;

        OrderStatusHistory deliveredStatus = new OrderStatusHistory();
        deliveredStatus.setNewState(DomainConstants.STATUS_DELIVERY);
        deliveredStatus.setStateChangeTime(LocalDateTime.now());
        deliveredStatus.setOwnerId(ownerId);

        List<OrderStatusHistory> historyList = List.of(deliveredStatus);

        when(orderStatusHistoryPersistencePort.findByOrderId(orderId)).thenReturn(historyList);
        when(authenticationSecurityPort.getAuthenticatedUserId()).thenReturn(ownerId);

        Exception exception = assertThrows(OrderStatusHistoryNotFoundException.class, () ->
                orderStatusHistoryUseCasePort.calculateOrderDuration(orderId));

        assertEquals(DomainConstants.ERROR_PENDING_NOT_FOUND, exception.getMessage());

        verify(orderStatusHistoryPersistencePort, times(1)).findByOrderId(orderId);
        verify(authenticationSecurityPort, times(1)).getAuthenticatedUserId();
    }

    @Test
    void calculateOrderDuration_ShouldThrowException_WhenDeliveredStatusNotFound() {
        Long orderId = 1L;
        Long ownerId = 2L;

        OrderStatusHistory pendingStatus = new OrderStatusHistory();
        pendingStatus.setNewState(DomainConstants.STATUS_PENDING);
        pendingStatus.setStateChangeTime(LocalDateTime.now().minusHours(2));
        pendingStatus.setOwnerId(ownerId);

        List<OrderStatusHistory> historyList = List.of(pendingStatus);

        when(orderStatusHistoryPersistencePort.findByOrderId(orderId)).thenReturn(historyList);
        when(authenticationSecurityPort.getAuthenticatedUserId()).thenReturn(ownerId);

        Exception exception = assertThrows(OrderStatusHistoryNotFoundException.class, () ->
                orderStatusHistoryUseCasePort.calculateOrderDuration(orderId));

        assertEquals(DomainConstants.ERROR_DELIVERED_NOT_FOUND, exception.getMessage());

        verify(orderStatusHistoryPersistencePort, times(1)).findByOrderId(orderId);
        verify(authenticationSecurityPort, times(1)).getAuthenticatedUserId();
    }

    @Test
    void calculateAverageOrderCompletionTimePerEmployee_ShouldReturnCorrectRanking() {
        Long authenticatedUserId = 2L;

        OrderStatusHistory orderStart = new OrderStatusHistory();
        orderStart.setOrderId(1L);
        orderStart.setNewState(DomainConstants.STATUS_IN_PREPARATION);
        orderStart.setStateChangeTime(LocalDateTime.now().minusHours(2));
        orderStart.setEmployeeId(10L);
        orderStart.setOwnerId(authenticatedUserId);

        OrderStatusHistory orderEnd = new OrderStatusHistory();
        orderEnd.setOrderId(1L);
        orderEnd.setNewState(DomainConstants.STATUS_DELIVERY);
        orderEnd.setStateChangeTime(LocalDateTime.now().minusHours(1));
        orderEnd.setOwnerId(authenticatedUserId);

        List<OrderStatusHistory> historyList = List.of(orderStart, orderEnd);

        when(orderStatusHistoryPersistencePort.findAll()).thenReturn(historyList);
        when(authenticationSecurityPort.getAuthenticatedUserId()).thenReturn(authenticatedUserId);

        List<EmployeeEfficiency> result = orderStatusHistoryUseCasePort.calculateAverageOrderCompletionTimePerEmployee();

        assertEquals(1, result.size());
        assertEquals(10L, result.get(0).getEmployeeId());
        assertEquals(Duration.ofHours(1), result.get(0).getDuration());

        verify(orderStatusHistoryPersistencePort, times(1)).findAll();
        verify(authenticationSecurityPort, times(1)).getAuthenticatedUserId();
    }

}