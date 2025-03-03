package microservice_traceability.traceability.infrastructure.persistence.mongodb.entity;

import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "order_status_history")
public class OrderStatusHistoryEntity {

    private String id;

    @Indexed
    private Long orderId;

    @Indexed
    private Long clientId;

    @Indexed
    private String customerMail;

    @Indexed
    private Long restaurantId;

    @Indexed
    private Long ownerId;

    @Indexed
    private Long employeeId;

    @Indexed
    private String employeeMail;

    @Indexed
    private String previousState;

    @Indexed
    private String newState;

    @Indexed
    private LocalDateTime stateChangeTime;

}
