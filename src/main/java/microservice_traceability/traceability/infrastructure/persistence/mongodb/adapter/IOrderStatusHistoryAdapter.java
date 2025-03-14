package microservice_traceability.traceability.infrastructure.persistence.mongodb.adapter;

import lombok.RequiredArgsConstructor;
import microservice_traceability.traceability.domain.model.OrderStatusHistory;
import microservice_traceability.traceability.domain.spi.IOrderStatusHistoryPersistencePort;
import microservice_traceability.traceability.infrastructure.persistence.mongodb.entity.OrderStatusHistoryEntity;
import microservice_traceability.traceability.infrastructure.persistence.mongodb.mapper.IOrderStatusHistoryEntityMapper;
import microservice_traceability.traceability.infrastructure.persistence.mongodb.repository.IOrderStatusHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IOrderStatusHistoryAdapter implements IOrderStatusHistoryPersistencePort {

    private final IOrderStatusHistoryRepository orderStatusHistoryRepository;
    private final IOrderStatusHistoryEntityMapper orderStatusHistoryEntityMapper;

    @Override
    public void saveOrderStatusHistory(OrderStatusHistory orderStatusHistory) {

        OrderStatusHistoryEntity entity = orderStatusHistoryEntityMapper.toEntity(orderStatusHistory);

        orderStatusHistoryRepository.save(entity);

    }

    @Override
    public String findCustomerMailByOrderId(Long orderId) {
        return orderStatusHistoryRepository.findTopByOrderId(orderId)
                .map(OrderStatusHistoryEntity::getCustomerMail)
                .orElse(null);
    }

    @Override
    public List<OrderStatusHistory> findByOrderId(Long orderId) {
        List<OrderStatusHistoryEntity> entities = orderStatusHistoryRepository.findByOrderId(orderId);
        return entities.stream()
                .map(orderStatusHistoryEntityMapper::toModel)
                .toList();
    }

    @Override
    public List<OrderStatusHistory> findAll() {
        return orderStatusHistoryRepository.findAll().stream()
                .map(orderStatusHistoryEntityMapper::toModel)
                .toList();
    }
}
