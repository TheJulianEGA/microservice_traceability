package microservice_traceability.traceability.application.mapper;

import microservice_traceability.traceability.application.dto.OrderStatusHistoryRequest;
import microservice_traceability.traceability.application.dto.OrderStatusHistoryResponse;
import microservice_traceability.traceability.domain.model.OrderStatusHistory;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusHistoryMapper implements IOrderStatusHistoryMapper {

    @Override
    public OrderStatusHistory toModel(OrderStatusHistoryRequest request) {
        return OrderStatusHistory.builder()
                .orderId(request.getOrderId())
                .clientId(request.getClientId())
                .customerMail(request.getCustomerMail())
                .restaurantId(request.getRestaurantId())
                .employeeId(request.getEmployeeId())
                .employeeMail(request.getEmployeeMail())
                .previousState(request.getPreviousState())
                .newState(request.getNewState())
                .stateChangeTime(request.getStateChangeTime())
                .build();
    }

    @Override
    public OrderStatusHistoryResponse toResponse(OrderStatusHistory orderStatusHistory) {
        return OrderStatusHistoryResponse.builder()
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
}
