package microservice_traceability.traceability.domain.api;

import microservice_traceability.traceability.domain.model.OrderStatusHistory;

import java.util.List;

public interface IOrderStatusHistoryServicePort {
    void saveOrderStatusHistory(OrderStatusHistory orderStatusHistory);

    List<OrderStatusHistory> getOrderStatusHistoryByOrderId(Long orderId);
}
