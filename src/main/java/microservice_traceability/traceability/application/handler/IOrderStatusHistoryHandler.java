package microservice_traceability.traceability.application.handler;

import microservice_traceability.traceability.application.dto.EmployeeEfficiencyResponse;
import microservice_traceability.traceability.application.dto.OrderStatusHistoryRequest;
import microservice_traceability.traceability.application.dto.OrderStatusHistoryResponse;
import microservice_traceability.traceability.application.dto.OrderTimeResponse;

import java.util.List;

public interface IOrderStatusHistoryHandler {
    void saveOrderStatusHistory(OrderStatusHistoryRequest request);

    List<OrderStatusHistoryResponse> getOrderStatusHistoryByOrderId(Long orderId);

    OrderTimeResponse getOrderTimeByOrderId(Long orderId);

    List<EmployeeEfficiencyResponse> getEmployeeEfficiencyRanking();

}
