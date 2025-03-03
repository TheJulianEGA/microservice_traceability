package microservice_traceability.traceability.infrastructure.persistence.mongodb.mapper;

import microservice_traceability.traceability.domain.model.OrderStatusHistory;
import microservice_traceability.traceability.infrastructure.persistence.mongodb.entity.OrderStatusHistoryEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderStatusHistoryEntityMapperTest {

    private OrderStatusHistoryEntityMapper orderStatusHistoryEntityMapper;

    @BeforeEach
    void setUp() {
        orderStatusHistoryEntityMapper = new OrderStatusHistoryEntityMapper();
    }

    @Test
    void toEntity_ShouldMapOrderStatusHistoryToEntity() {
        LocalDateTime now = LocalDateTime.now();

        OrderStatusHistory orderStatusHistory = OrderStatusHistory.builder()
                .orderId(1L)
                .clientId(2L)
                .customerMail("customer@example.com")
                .restaurantId(3L)
                .employeeId(4L)
                .employeeMail("employee@example.com")
                .previousState("PENDING")
                .newState("DELIVERED")
                .stateChangeTime(now)
                .build();

        OrderStatusHistoryEntity entity = orderStatusHistoryEntityMapper.toEntity(orderStatusHistory);

        assertNotNull(entity);
        assertAll(
                () -> assertEquals(orderStatusHistory.getOrderId(), entity.getOrderId()),
                () -> assertEquals(orderStatusHistory.getClientId(), entity.getClientId()),
                () -> assertEquals(orderStatusHistory.getCustomerMail(), entity.getCustomerMail()),
                () -> assertEquals(orderStatusHistory.getRestaurantId(), entity.getRestaurantId()),
                () -> assertEquals(orderStatusHistory.getEmployeeId(), entity.getEmployeeId()),
                () -> assertEquals(orderStatusHistory.getEmployeeMail(), entity.getEmployeeMail()),
                () -> assertEquals(orderStatusHistory.getPreviousState(), entity.getPreviousState()),
                () -> assertEquals(orderStatusHistory.getNewState(), entity.getNewState()),
                () -> assertEquals(orderStatusHistory.getStateChangeTime(), entity.getStateChangeTime())
        );
    }

    @Test
    void toModel_ShouldMapEntityToOrderStatusHistory() {
        LocalDateTime now = LocalDateTime.now();

        OrderStatusHistoryEntity entity = OrderStatusHistoryEntity.builder()
                .orderId(1L)
                .clientId(2L)
                .customerMail("customer@example.com")
                .restaurantId(3L)
                .employeeId(4L)
                .employeeMail("employee@example.com")
                .previousState("PENDING")
                .newState("DELIVERED")
                .stateChangeTime(now)
                .build();

        OrderStatusHistory orderStatusHistory = orderStatusHistoryEntityMapper.toModel(entity);

        assertNotNull(orderStatusHistory);
        assertAll(
                () -> assertEquals(entity.getOrderId(), orderStatusHistory.getOrderId()),
                () -> assertEquals(entity.getClientId(), orderStatusHistory.getClientId()),
                () -> assertEquals(entity.getCustomerMail(), orderStatusHistory.getCustomerMail()),
                () -> assertEquals(entity.getRestaurantId(), orderStatusHistory.getRestaurantId()),
                () -> assertEquals(entity.getEmployeeId(), orderStatusHistory.getEmployeeId()),
                () -> assertEquals(entity.getEmployeeMail(), orderStatusHistory.getEmployeeMail()),
                () -> assertEquals(entity.getPreviousState(), orderStatusHistory.getPreviousState()),
                () -> assertEquals(entity.getNewState(), orderStatusHistory.getNewState()),
                () -> assertEquals(entity.getStateChangeTime(), orderStatusHistory.getStateChangeTime())
        );
    }
}