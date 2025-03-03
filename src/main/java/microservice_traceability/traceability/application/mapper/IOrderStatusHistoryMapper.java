package microservice_traceability.traceability.application.mapper;

import microservice_traceability.traceability.application.dto.OrderStatusHistoryRequest;
import microservice_traceability.traceability.application.dto.OrderStatusHistoryResponse;
import microservice_traceability.traceability.domain.model.OrderStatusHistory;

public interface IOrderStatusHistoryMapper {

    OrderStatusHistory toModel(OrderStatusHistoryRequest request);

    OrderStatusHistoryResponse toResponse(OrderStatusHistory orderStatusHistory);
}
