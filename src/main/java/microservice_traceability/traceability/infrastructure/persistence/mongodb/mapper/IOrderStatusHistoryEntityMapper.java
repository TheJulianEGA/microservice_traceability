package microservice_traceability.traceability.infrastructure.persistence.mongodb.mapper;

import microservice_traceability.traceability.domain.model.OrderStatusHistory;
import microservice_traceability.traceability.infrastructure.persistence.mongodb.entity.OrderStatusHistoryEntity;

public interface IOrderStatusHistoryEntityMapper {

    OrderStatusHistoryEntity toEntity (OrderStatusHistory orderStatusHistory);

    OrderStatusHistory toModel(OrderStatusHistoryEntity orderStatusHistoryEntity);
}
