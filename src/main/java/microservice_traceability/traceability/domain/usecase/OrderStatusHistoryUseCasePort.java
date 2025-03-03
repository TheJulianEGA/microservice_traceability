package microservice_traceability.traceability.domain.usecase;

import lombok.RequiredArgsConstructor;
import microservice_traceability.traceability.domain.api.IOrderStatusHistoryServicePort;
import microservice_traceability.traceability.domain.exception.OrderAccessForbiddenException;
import microservice_traceability.traceability.domain.exception.OrderStatusHistoryNotFoundException;
import microservice_traceability.traceability.domain.model.OrderStatusHistory;
import microservice_traceability.traceability.domain.security.IAuthenticationSecurityPort;
import microservice_traceability.traceability.domain.spi.IOrderStatusHistoryPersistencePort;
import microservice_traceability.traceability.domain.util.DomainConstants;

import java.util.List;

@RequiredArgsConstructor
public class OrderStatusHistoryUseCasePort implements IOrderStatusHistoryServicePort {

    private final IOrderStatusHistoryPersistencePort orderStatusHistoryPersistencePort;
    private final IAuthenticationSecurityPort authenticationSecurityPort;

    @Override
    public void saveOrderStatusHistory(OrderStatusHistory orderStatusHistory) {
        setCustomerMailIfMissing(orderStatusHistory);

        orderStatusHistoryPersistencePort.saveOrderStatusHistory(orderStatusHistory);
    }

    @Override
    public List<OrderStatusHistory> getOrderStatusHistoryByOrderId(Long orderId) {
        List<OrderStatusHistory> historyList = orderStatusHistoryPersistencePort.findByOrderId(orderId);

        validateOrderHistoryExists(historyList);
        validateClientAccess(historyList);

        return historyList;
    }

    private void setCustomerMailIfMissing(OrderStatusHistory orderStatusHistory) {
        if (orderStatusHistory.getCustomerMail() == null || orderStatusHistory.getCustomerMail().isBlank()) {
            String existingCustomerMail = orderStatusHistoryPersistencePort
                    .findCustomerMailByOrderId(orderStatusHistory.getOrderId());
            if (existingCustomerMail != null) {
                orderStatusHistory.setCustomerMail(existingCustomerMail);
            }
        }
    }


    private void validateOrderHistoryExists(List<OrderStatusHistory> historyList) {
        if (historyList.isEmpty()) {
            throw new OrderStatusHistoryNotFoundException(DomainConstants.NO_ORDER_HISTORY_FOUND);
        }
    }

    private void validateClientAccess(List<OrderStatusHistory> historyList) {
        Long authenticatedUserId = authenticationSecurityPort.getAuthenticatedUserId();
        Long clientId = historyList.get(0).getClientId();

        if (!clientId.equals(authenticatedUserId)) {
            throw new OrderAccessForbiddenException(DomainConstants.ACCESS_FORBIDDEN);
        }
    }

}

