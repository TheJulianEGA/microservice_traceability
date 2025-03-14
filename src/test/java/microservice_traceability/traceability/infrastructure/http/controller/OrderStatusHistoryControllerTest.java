package microservice_traceability.traceability.infrastructure.http.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import microservice_traceability.traceability.application.dto.EmployeeEfficiencyResponse;
import microservice_traceability.traceability.application.dto.OrderStatusHistoryRequest;
import microservice_traceability.traceability.application.dto.OrderStatusHistoryResponse;
import microservice_traceability.traceability.application.dto.OrderTimeResponse;
import microservice_traceability.traceability.application.handler.IOrderStatusHistoryHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderStatusHistoryControllerTest {

    @InjectMocks
    private OrderStatusHistoryController orderStatusHistoryController;

    @Mock
    private IOrderStatusHistoryHandler orderStatusHistoryHandler;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(orderStatusHistoryController).build();

    }

    @Test
    void saveOrderStatusHistory_ShouldReturnCreated_WhenRequestIsValid() throws Exception {
        OrderStatusHistoryRequest request = OrderStatusHistoryRequest.builder()
                .orderId(1L)
                .clientId(2L)
                .customerMail("customer@example.com")
                .restaurantId(3L)
                .employeeId(4L)
                .employeeMail("employee@example.com")
                .previousState("PENDING")
                .newState("IN_PROGRESS")
                .stateChangeTime(LocalDateTime.now())
                .build();

        doNothing().when(orderStatusHistoryHandler).saveOrderStatusHistory(any(OrderStatusHistoryRequest.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/order-status-history/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        verify(orderStatusHistoryHandler, times(1)).saveOrderStatusHistory(any(OrderStatusHistoryRequest.class));
    }

    @Test
    void getOrderStatusHistoryByOrderId_ShouldReturnOk_WhenHistoryExists() throws Exception {
        Long orderId = 1L;
        List<OrderStatusHistoryResponse> responseList = List.of(
                OrderStatusHistoryResponse.builder()
                        .orderId(orderId)
                        .clientId(2L)
                        .customerMail("customer@example.com")
                        .restaurantId(3L)
                        .employeeId(4L)
                        .employeeMail("employee@example.com")
                        .previousState("PENDING")
                        .newState("IN_PROGRESS")
                        .stateChangeTime(LocalDateTime.now())
                        .build()
        );

        when(orderStatusHistoryHandler.getOrderStatusHistoryByOrderId(orderId)).thenReturn(responseList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/order-status-history/list-by-order/{orderId}", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(responseList)));

        verify(orderStatusHistoryHandler, times(1)).getOrderStatusHistoryByOrderId(orderId);
    }

    @Test
    void getOrderTimeByOrderId_ShouldReturnOk_WhenOrderExists() throws Exception {
        Long orderId = 1L;
        OrderTimeResponse response = new OrderTimeResponse(orderId, 3600, 60, 1);

        when(orderStatusHistoryHandler.getOrderTimeByOrderId(orderId)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/order-status-history/time-per-order/{orderId}", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(response)));

        verify(orderStatusHistoryHandler, times(1)).getOrderTimeByOrderId(orderId);
    }

    @Test
    void getEmployeeEfficiencyRanking_ShouldReturnOk_WhenDataExists() throws Exception {
        List<EmployeeEfficiencyResponse> responseList = List.of(
                new EmployeeEfficiencyResponse(1L, 1800, 30, 0),
                new EmployeeEfficiencyResponse(2L, 3600, 60, 1)
        );

        when(orderStatusHistoryHandler.getEmployeeEfficiencyRanking()).thenReturn(responseList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/order-status-history/employee-ranking")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(responseList)));

        verify(orderStatusHistoryHandler, times(1)).getEmployeeEfficiencyRanking();
    }
}