package microservice_traceability.traceability.application.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeEfficiencyResponse {

    private Long employeeId;
    private long averageHours;
    private long averageMinutes;
    private long averageSeconds;

}
