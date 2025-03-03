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

        OrderStatusHistory orderStatusHistory = new OrderStatusHistory();
        orderStatusHistory.setOrderId(1L);
        orderStatusHistory.setClientId(2L);
        orderStatusHistory.setCustomerMail("customer@example.com");
        orderStatusHistory.setRestaurantId(3L);
        orderStatusHistory.setEmployeeId(4L);
        orderStatusHistory.setEmployeeMail("employee@example.com");
        orderStatusHistory.setPreviousState("PENDING");
        orderStatusHistory.setNewState("DELIVERED");
        orderStatusHistory.setStateChangeTime(LocalDateTime.now());

        OrderStatusHistoryEntity entity = orderStatusHistoryEntityMapper.toEntity(orderStatusHistory);

        assertNotNull(entity);
        assertEquals(orderStatusHistory.getOrderId(), entity.getOrderId());
        assertEquals(orderStatusHistory.getClientId(), entity.getClientId());
        assertEquals(orderStatusHistory.getCustomerMail(), entity.getCustomerMail());
        assertEquals(orderStatusHistory.getRestaurantId(), entity.getRestaurantId());
        assertEquals(orderStatusHistory.getEmployeeId(), entity.getEmployeeId());
        assertEquals(orderStatusHistory.getEmployeeMail(), entity.getEmployeeMail());
        assertEquals(orderStatusHistory.getPreviousState(), entity.getPreviousState());
        assertEquals(orderStatusHistory.getNewState(), entity.getNewState());
        assertEquals(orderStatusHistory.getStateChangeTime(), entity.getStateChangeTime());
    }
}