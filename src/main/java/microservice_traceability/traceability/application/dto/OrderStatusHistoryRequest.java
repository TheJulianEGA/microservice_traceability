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
public class OrderStatusHistoryRequest {

    @NotNull
    private Long orderId;

    @NotNull
    private Long clientId;

    private String customerMail;

    @NotNull
    private Long restaurantId;

    private Long employeeId;

    private String employeeMail;

    private String previousState;

    @NotBlank
    private String newState;

    @NotNull
    private LocalDateTime stateChangeTime;

}
