package microservice_traceability.traceability.domain.spi;

import microservice_traceability.traceability.domain.model.OrderStatusHistory;

import java.util.List;

public interface IOrderStatusHistoryPersistencePort {
    void saveOrderStatusHistory(OrderStatusHistory orderStatusHistory);

    String findCustomerMailByOrderId(Long orderId);

    List<OrderStatusHistory> findByOrderId(Long orderId);
}
