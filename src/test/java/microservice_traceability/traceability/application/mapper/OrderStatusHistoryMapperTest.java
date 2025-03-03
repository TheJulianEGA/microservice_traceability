package microservice_traceability.traceability.application.mapper;

import microservice_traceability.traceability.application.dto.OrderStatusHistoryRequest;
import microservice_traceability.traceability.application.dto.OrderStatusHistoryResponse;
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
        LocalDateTime now = LocalDateTime.now();

        OrderStatusHistoryRequest request = OrderStatusHistoryRequest.builder()
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

        OrderStatusHistory model = orderStatusHistoryMapper.toModel(request);

        assertNotNull(model);
        assertAll(
                () -> assertEquals(request.getOrderId(), model.getOrderId()),
                () -> assertEquals(request.getClientId(), model.getClientId()),
                () -> assertEquals(request.getCustomerMail(), model.getCustomerMail()),
                () -> assertEquals(request.getRestaurantId(), model.getRestaurantId()),
                () -> assertEquals(request.getEmployeeId(), model.getEmployeeId()),
                () -> assertEquals(request.getEmployeeMail(), model.getEmployeeMail()),
                () -> assertEquals(request.getPreviousState(), model.getPreviousState()),
                () -> assertEquals(request.getNewState(), model.getNewState()),
                () -> assertEquals(request.getStateChangeTime(), model.getStateChangeTime())
        );
    }

    @Test
    void toResponse_ShouldMapModelToResponse() {
        LocalDateTime now = LocalDateTime.now();

        OrderStatusHistory model = OrderStatusHistory.builder()
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

        OrderStatusHistoryResponse response = orderStatusHistoryMapper.toResponse(model);

        assertNotNull(response);
        assertAll(
                () -> assertEquals(model.getOrderId(), response.getOrderId()),
                () -> assertEquals(model.getClientId(), response.getClientId()),
                () -> assertEquals(model.getCustomerMail(), response.getCustomerMail()),
                () -> assertEquals(model.getRestaurantId(), response.getRestaurantId()),
                () -> assertEquals(model.getEmployeeId(), response.getEmployeeId()),
                () -> assertEquals(model.getEmployeeMail(), response.getEmployeeMail()),
                () -> assertEquals(model.getPreviousState(), response.getPreviousState()),
                () -> assertEquals(model.getNewState(), response.getNewState()),
                () -> assertEquals(model.getStateChangeTime(), response.getStateChangeTime())
        );
    }
}