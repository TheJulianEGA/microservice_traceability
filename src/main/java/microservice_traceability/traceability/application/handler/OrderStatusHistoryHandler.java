package microservice_traceability.traceability.application.handler;

import lombok.RequiredArgsConstructor;
import microservice_traceability.traceability.application.dto.EmployeeEfficiencyResponse;
import microservice_traceability.traceability.application.dto.OrderStatusHistoryRequest;
import microservice_traceability.traceability.application.dto.OrderStatusHistoryResponse;
import microservice_traceability.traceability.application.dto.OrderTimeResponse;
import microservice_traceability.traceability.application.mapper.IOrderStatusHistoryMapper;
import microservice_traceability.traceability.domain.api.IOrderStatusHistoryServicePort;
import microservice_traceability.traceability.domain.model.OrderStatusHistory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderStatusHistoryHandler implements IOrderStatusHistoryHandler{

    private final IOrderStatusHistoryMapper orderStatusHistoryMapper;
    private final IOrderStatusHistoryServicePort orderStatusHistoryService;

    @Override
    public void saveOrderStatusHistory(OrderStatusHistoryRequest request) {
        OrderStatusHistory orderStatusHistory = orderStatusHistoryMapper.toModel(request);
        orderStatusHistoryService.saveOrderStatusHistory(orderStatusHistory);
    }

    @Override
    public List<OrderStatusHistoryResponse> getOrderStatusHistoryByOrderId(Long orderId) {
        List<OrderStatusHistory> historyList = orderStatusHistoryService.getOrderStatusHistoryByOrderId(orderId);
        return historyList.stream()
                .map(orderStatusHistoryMapper::toResponse)
                .toList();
    }

    @Override
    public OrderTimeResponse getOrderTimeByOrderId(Long orderId) {
        Duration duration = orderStatusHistoryService.calculateOrderDuration(orderId);
        return orderStatusHistoryMapper.toOrderTimeResponse(orderId, duration);
    }

    @Override
    public List<EmployeeEfficiencyResponse> getEmployeeEfficiencyRanking() {
        return orderStatusHistoryService.calculateAverageOrderCompletionTimePerEmployee().stream()
                .map(orderStatusHistoryMapper::toEmployeeEfficiencyResponse)
                .toList();
    }
}
