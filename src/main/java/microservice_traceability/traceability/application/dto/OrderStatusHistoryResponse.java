package microservice_traceability.traceability.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatusHistoryResponse {

    private Long orderId;

    private Long clientId;

    private String customerMail;

    private Long restaurantId;

    private Long employeeId;

    private String employeeMail;

    private String previousState;

    private String newState;

    private LocalDateTime stateChangeTime;

}
