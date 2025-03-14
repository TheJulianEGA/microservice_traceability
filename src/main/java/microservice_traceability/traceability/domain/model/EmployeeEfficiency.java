package microservice_traceability.traceability.domain.model;

import lombok.*;

import java.time.Duration;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeEfficiency {

    private Long employeeId;
    private Duration duration;

}
