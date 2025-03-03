package microservice_traceability.traceability.infrastructure.persistence.mongodb.adapter;

import microservice_traceability.traceability.domain.model.OrderStatusHistory;
import microservice_traceability.traceability.infrastructure.persistence.mongodb.entity.OrderStatusHistoryEntity;
import microservice_traceability.traceability.infrastructure.persistence.mongodb.mapper.IOrderStatusHistoryEntityMapper;
import microservice_traceability.traceability.infrastructure.persistence.mongodb.repository.IOrderStatusHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IOrderStatusHistoryAdapterTest {

    @Mock
    private IOrderStatusHistoryRepository orderStatusHistoryRepository;

    @Mock
    private IOrderStatusHistoryEntityMapper orderStatusHistoryEntityMapper;

    @InjectMocks
    private IOrderStatusHistoryAdapter orderStatusHistoryAdapter;

    private OrderStatusHistory orderStatusHistory;
    private OrderStatusHistoryEntity orderStatusHistoryEntity;

    @BeforeEach
    void setUp() {
        orderStatusHistory = OrderStatusHistory.builder()
                .orderId(1L)
                .customerMail("customer@example.com")
                .build();

        orderStatusHistoryEntity = OrderStatusHistoryEntity.builder()
                .orderId(1L)
                .customerMail("customer@example.com")
                .build();
    }

    @Test
    void saveOrderStatusHistory_ShouldSaveEntity_WhenValidOrderStatusHistory() {
        when(orderStatusHistoryEntityMapper.toEntity(orderStatusHistory)).thenReturn(orderStatusHistoryEntity);

        orderStatusHistoryAdapter.saveOrderStatusHistory(orderStatusHistory);

        verify(orderStatusHistoryEntityMapper, times(1)).toEntity(orderStatusHistory);
        verify(orderStatusHistoryRepository, times(1)).save(orderStatusHistoryEntity);
    }

    @Test
    void findCustomerMailByOrderId_ShouldReturnMail_WhenOrderExists() {
        when(orderStatusHistoryRepository.findTopByOrderId(1L)).thenReturn(Optional.of(orderStatusHistoryEntity));

        String email = orderStatusHistoryAdapter.findCustomerMailByOrderId(1L);

        assertNotNull(email);
        assertEquals("customer@example.com", email);
    }

    @Test
    void findCustomerMailByOrderId_ShouldReturnNull_WhenOrderDoesNotExist() {
        when(orderStatusHistoryRepository.findTopByOrderId(1L)).thenReturn(Optional.empty());

        String email = orderStatusHistoryAdapter.findCustomerMailByOrderId(1L);

        assertNull(email);
    }
}
