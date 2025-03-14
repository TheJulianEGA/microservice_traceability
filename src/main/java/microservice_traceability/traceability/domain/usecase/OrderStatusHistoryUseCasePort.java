package microservice_traceability.traceability.domain.usecase;

import lombok.RequiredArgsConstructor;
import microservice_traceability.traceability.domain.api.IOrderStatusHistoryServicePort;
import microservice_traceability.traceability.domain.exception.OrderAccessForbiddenException;
import microservice_traceability.traceability.domain.exception.OrderStatusHistoryNotFoundException;
import microservice_traceability.traceability.domain.model.EmployeeEfficiency;
import microservice_traceability.traceability.domain.model.OrderStatusHistory;
import microservice_traceability.traceability.domain.security.IAuthenticationSecurityPort;
import microservice_traceability.traceability.domain.spi.IOrderStatusHistoryPersistencePort;
import microservice_traceability.traceability.domain.util.DomainConstants;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class OrderStatusHistoryUseCasePort implements IOrderStatusHistoryServicePort {

    private final IOrderStatusHistoryPersistencePort orderStatusHistoryPersistencePort;
    private final IAuthenticationSecurityPort authenticationSecurityPort;

    @Override
    public void saveOrderStatusHistory(OrderStatusHistory orderStatusHistory) {
        List<OrderStatusHistory> historyList = orderStatusHistoryPersistencePort.findByOrderId(orderStatusHistory.getOrderId());

        setCustomerMailIfMissing(orderStatusHistory, historyList);
        setOwnerIdIfMissing(orderStatusHistory, historyList);

        orderStatusHistoryPersistencePort.saveOrderStatusHistory(orderStatusHistory);
    }

    @Override
    public List<OrderStatusHistory> getOrderStatusHistoryByOrderId(Long orderId) {
        List<OrderStatusHistory> historyList = orderStatusHistoryPersistencePort.findByOrderId(orderId);

        validateOrderHistoryExists(historyList);
        validateClientAccess(historyList);

        return historyList;
    }

    @Override
    public Duration calculateOrderDuration(Long orderId) {
        List<OrderStatusHistory> historyList = orderStatusHistoryPersistencePort.findByOrderId(orderId);

        validateOrderHistoryExists(historyList);
        validateOwnerAccess(historyList);

        LocalDateTime orderStartTime = findOrderStartTime(historyList);
        LocalDateTime orderEndTime = findOrderEndTime(historyList);

        return Duration.between(orderStartTime, orderEndTime);
    }

    @Override
    public List<EmployeeEfficiency> calculateAverageOrderCompletionTimePerEmployee() {
        List<OrderStatusHistory> historyList = orderStatusHistoryPersistencePort.findAll();
        validateOwnerAccess(historyList);

        Map<Long, List<Duration>> employeeDurations = calculateDurationsGroupedByEmployee(historyList);

        return employeeDurations.entrySet().stream()
                .map(entry -> createEmployeeEfficiency(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(EmployeeEfficiency::getDuration))
                .toList();
    }

    private LocalDateTime findOrderStartTime(List<OrderStatusHistory> historyList) {

        return historyList.stream()
                .filter(h -> DomainConstants.STATUS_PENDING.equalsIgnoreCase(h.getNewState()))
                .map(OrderStatusHistory::getStateChangeTime)
                .findFirst()
                .orElseThrow(() -> new OrderStatusHistoryNotFoundException(DomainConstants.ERROR_PENDING_NOT_FOUND ));
    }

    private LocalDateTime findOrderEndTime(List<OrderStatusHistory> historyList) {
        return historyList.stream()
                .filter(h -> DomainConstants.STATUS_DELIVERY.equalsIgnoreCase(h.getNewState()))
                .map(OrderStatusHistory::getStateChangeTime)
                .reduce((first, second) -> second)
                .orElseThrow(() -> new OrderStatusHistoryNotFoundException( DomainConstants.ERROR_DELIVERED_NOT_FOUND));
    }

    private Map<Long, List<Duration>> calculateDurationsGroupedByEmployee(List<OrderStatusHistory> historyList) {
        Map<Long, List<Duration>> employeeDurations = new HashMap<>();

        historyList.stream()
                .collect(Collectors.groupingBy(OrderStatusHistory::getOrderId))
                .forEach((orderId, orderHistory) ->
                        processOrderHistory(orderHistory, employeeDurations));

        return employeeDurations;
    }

    private void processOrderHistory(List<OrderStatusHistory> orderHistory, Map<Long, List<Duration>> employeeDurations) {

        LocalDateTime startTime = findStateChangeTime(orderHistory, DomainConstants.STATUS_IN_PREPARATION);
        LocalDateTime endTime = findStateChangeTime(orderHistory, DomainConstants.STATUS_DELIVERY);

        if (startTime != null && endTime != null) {
            Long employeeId = findEmployeeId(orderHistory);
            if (employeeId != null) {
                employeeDurations.computeIfAbsent(employeeId, k -> new ArrayList<>())
                        .add(Duration.between(startTime, endTime));
            }
        }
    }

    private LocalDateTime findStateChangeTime(List<OrderStatusHistory> orderHistory, String targetState) {
        return orderHistory.stream()
                .filter(h -> targetState.equals(h.getNewState()))
                .map(OrderStatusHistory::getStateChangeTime)
                .findFirst()
                .orElse(null);
    }

    private Long findEmployeeId(List<OrderStatusHistory> orderHistory) {
        return orderHistory.stream()
                .filter(h -> h.getEmployeeId() != null)
                .map(OrderStatusHistory::getEmployeeId)
                .findFirst()
                .orElse(null);
    }

    private EmployeeEfficiency createEmployeeEfficiency(Long employeeId, List<Duration> durations) {
        Duration averageDuration = durations.stream()
                .reduce(Duration.ZERO, Duration::plus)
                .dividedBy(durations.size());

        return new EmployeeEfficiency(employeeId, averageDuration);
    }

    private void setCustomerMailIfMissing(OrderStatusHistory orderStatusHistory, List<OrderStatusHistory> historyList) {
        if (orderStatusHistory.getCustomerMail() == null) {
            historyList.stream()
                    .map(OrderStatusHistory::getCustomerMail)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .ifPresent(orderStatusHistory::setCustomerMail);
        }
    }

    private void setOwnerIdIfMissing(OrderStatusHistory orderStatusHistory, List<OrderStatusHistory> historyList) {
        if (orderStatusHistory.getOwnerId() == null) {
            historyList.stream()
                    .map(OrderStatusHistory::getOwnerId)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .ifPresent(orderStatusHistory::setOwnerId);
        }
    }


    private void validateOrderHistoryExists(List<OrderStatusHistory> historyList) {
        if (historyList.isEmpty()) {
            throw new OrderStatusHistoryNotFoundException(DomainConstants.NO_ORDER_HISTORY_FOUND);
        }
    }

    private void validateClientAccess(List<OrderStatusHistory> historyList) {
        Long authenticatedUserId = authenticationSecurityPort.getAuthenticatedUserId();
        Long clientId = historyList.get(0).getClientId();

        if (!clientId.equals(authenticatedUserId)) {
            throw new OrderAccessForbiddenException(DomainConstants.ACCESS_FORBIDDEN);
        }
    }

    private void validateOwnerAccess(List<OrderStatusHistory> historyList) {
        Long authenticatedUserId = authenticationSecurityPort.getAuthenticatedUserId();
        Long clientId = historyList.get(0).getOwnerId();

        if (!clientId.equals(authenticatedUserId)) {
            throw new OrderAccessForbiddenException(DomainConstants.ACCESS_FORBIDDEN);
        }
    }

}

