package microservice_traceability.traceability.application.mapper;

import microservice_traceability.traceability.application.dto.EmployeeEfficiencyResponse;
import microservice_traceability.traceability.application.dto.OrderStatusHistoryRequest;
import microservice_traceability.traceability.application.dto.OrderStatusHistoryResponse;
import microservice_traceability.traceability.application.dto.OrderTimeResponse;
import microservice_traceability.traceability.domain.model.EmployeeEfficiency;
import microservice_traceability.traceability.domain.model.OrderStatusHistory;

import java.time.Duration;

public interface IOrderStatusHistoryMapper {

    OrderStatusHistory toModel(OrderStatusHistoryRequest request);

    OrderStatusHistoryResponse toResponse(OrderStatusHistory orderStatusHistory);

    OrderTimeResponse toOrderTimeResponse(Long orderId, Duration duration);

    EmployeeEfficiencyResponse toEmployeeEfficiencyResponse(EmployeeEfficiency employeeEfficiency);

}
