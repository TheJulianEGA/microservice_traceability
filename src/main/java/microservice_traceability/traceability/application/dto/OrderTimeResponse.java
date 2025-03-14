package microservice_traceability.traceability.application.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderTimeResponse {

    private Long orderId;
    private long hours;
    private long minutes;
    private long seconds;

}
