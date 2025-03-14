package microservice_traceability.traceability.infrastructure.persistence.mongodb.repository;

import microservice_traceability.traceability.infrastructure.persistence.mongodb.entity.OrderStatusHistoryEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface IOrderStatusHistoryRepository extends MongoRepository<OrderStatusHistoryEntity,String> {

    Optional<OrderStatusHistoryEntity> findTopByOrderId(Long orderId);

    List<OrderStatusHistoryEntity> findByOrderId(Long orderId);
}
