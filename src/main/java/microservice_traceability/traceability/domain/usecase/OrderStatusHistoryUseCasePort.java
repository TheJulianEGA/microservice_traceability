package microservice_traceability.traceability.domain.usecase;

import lombok.RequiredArgsConstructor;
import microservice_traceability.traceability.domain.api.IOrderStatusHistoryServicePort;
import microservice_traceability.traceability.domain.exception.OrderAccessForbiddenException;
import microservice_traceability.traceability.domain.exception.OrderStatusHistoryNotFoundException;
import microservice_traceability.traceability.domain.model.OrderStatusHistory;
import microservice_traceability.traceability.domain.security.IAuthenticationSecurityPort;
import microservice_traceability.traceability.domain.spi.IOrderStatusHistoryPersistencePort;

import java.util.List;

@RequiredArgsConstructor
public class OrderStatusHistoryUseCasePort implements IOrderStatusHistoryServicePort {

    private final IOrderStatusHistoryPersistencePort orderStatusHistoryPersistencePort;
    private final IAuthenticationSecurityPort authenticationSecurityPort;

    @Override
    public void saveOrderStatusHistory(OrderStatusHistory orderStatusHistory) {
        if (orderStatusHistory.getCustomerMail() == null || orderStatusHistory.getCustomerMail().isBlank()) {
            String existingCustomerMail = orderStatusHistoryPersistencePort
                    .findCustomerMailByOrderId(orderStatusHistory.getOrderId());
            if (existingCustomerMail != null) {
                orderStatusHistory.setCustomerMail(existingCustomerMail);
            }
        }

        orderStatusHistoryPersistencePort.saveOrderStatusHistory(orderStatusHistory);
    }

    @Override
    public List<OrderStatusHistory> getOrderStatusHistoryByOrderId(Long orderId) {
        List<OrderStatusHistory> historyList = orderStatusHistoryPersistencePort.findByOrderId(orderId);

        if (historyList.isEmpty()) {
            throw new OrderStatusHistoryNotFoundException("No order status history found for orderId: " + orderId);
        }

        Long authenticatedUserId = authenticationSecurityPort.getAuthenticatedUserId();
        if (!historyList.get(0).getClientId().equals(authenticatedUserId)) {
            throw new OrderAccessForbiddenException("You do not have permission to access this order history.");
        }

        return historyList;
    }
}

