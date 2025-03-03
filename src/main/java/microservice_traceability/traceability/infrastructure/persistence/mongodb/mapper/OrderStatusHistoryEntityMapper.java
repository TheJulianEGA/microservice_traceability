package microservice_traceability.traceability.infrastructure.persistence.mongodb.mapper;

import microservice_traceability.traceability.domain.model.OrderStatusHistory;
import microservice_traceability.traceability.infrastructure.persistence.mongodb.entity.OrderStatusHistoryEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusHistoryEntityMapper implements IOrderStatusHistoryEntityMapper {

    @Override
    public OrderStatusHistoryEntity toEntity(OrderStatusHistory orderStatusHistory) {
        return OrderStatusHistoryEntity.builder()
                .orderId(orderStatusHistory.getOrderId())
                .clientId(orderStatusHistory.getClientId())
                .customerMail(orderStatusHistory.getCustomerMail())
                .restaurantId(orderStatusHistory.getRestaurantId())
                .employeeId(orderStatusHistory.getEmployeeId())
                .employeeMail(orderStatusHistory.getEmployeeMail())
                .previousState(orderStatusHistory.getPreviousState())
                .newState(orderStatusHistory.getNewState())
                .stateChangeTime(orderStatusHistory.getStateChangeTime())
                .build();
    }

    @Override
    public OrderStatusHistory toModel(OrderStatusHistoryEntity orderStatusHistoryEntity) {
        return OrderStatusHistory.builder()
                .orderId(orderStatusHistoryEntity.getOrderId())
                .clientId(orderStatusHistoryEntity.getClientId())
                .customerMail(orderStatusHistoryEntity.getCustomerMail())
                .restaurantId(orderStatusHistoryEntity.getRestaurantId())
                .employeeId(orderStatusHistoryEntity.getEmployeeId())
                .employeeMail(orderStatusHistoryEntity.getEmployeeMail())
                .previousState(orderStatusHistoryEntity.getPreviousState())
                .newState(orderStatusHistoryEntity.getNewState())
                .stateChangeTime(orderStatusHistoryEntity.getStateChangeTime())
                .build();
    }
}
