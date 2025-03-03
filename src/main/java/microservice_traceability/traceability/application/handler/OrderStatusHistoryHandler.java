package microservice_traceability.traceability.application.handler;

import lombok.RequiredArgsConstructor;
import microservice_traceability.traceability.application.dto.OrderStatusHistoryRequest;
import microservice_traceability.traceability.application.dto.OrderStatusHistoryResponse;
import microservice_traceability.traceability.application.mapper.IOrderStatusHistoryMapper;
import microservice_traceability.traceability.domain.api.IOrderStatusHistoryServicePort;
import microservice_traceability.traceability.domain.model.OrderStatusHistory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
                .collect(Collectors.toList());
    }
}
