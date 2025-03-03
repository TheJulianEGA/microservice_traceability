package microservice_traceability.traceability.application.mapper;

import microservice_traceability.traceability.application.dto.OrderStatusHistoryRequest;
import microservice_traceability.traceability.domain.model.OrderStatusHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderStatusHistoryMapperTest {

    private OrderStatusHistoryMapper orderStatusHistoryMapper;

    @BeforeEach
    void setUp() {
        orderStatusHistoryMapper = new OrderStatusHistoryMapper();
    }

    @Test
    void toModel_ShouldMapRequestToModel() {

        OrderStatusHistoryRequest request = new OrderStatusHistoryRequest();
        request.setOrderId(1L);
        request.setClientId(2L);
        request.setCustomerMail("customer@example.com");
        request.setRestaurantId(3L);
        request.setEmployeeId(4L);
        request.setEmployeeMail("employee@example.com");
        request.setPreviousState("PENDING");
        request.setNewState("DELIVERED");
        request.setStateChangeTime(LocalDateTime.now());

        OrderStatusHistory model = orderStatusHistoryMapper.toModel(request);

        assertNotNull(model);
        assertEquals(request.getOrderId(), model.getOrderId());
        assertEquals(request.getClientId(), model.getClientId());
        assertEquals(request.getCustomerMail(), model.getCustomerMail());
        assertEquals(request.getRestaurantId(), model.getRestaurantId());
        assertEquals(request.getEmployeeId(), model.getEmployeeId());
        assertEquals(request.getEmployeeMail(), model.getEmployeeMail());
        assertEquals(request.getPreviousState(), model.getPreviousState());
        assertEquals(request.getNewState(), model.getNewState());
        assertEquals(request.getStateChangeTime(), model.getStateChangeTime());
    }
}
