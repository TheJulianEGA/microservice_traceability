package microservice_traceability.traceability.infrastructure.configuration;

import microservice_traceability.traceability.domain.api.IOrderStatusHistoryServicePort;
import microservice_traceability.traceability.domain.security.IAuthenticationSecurityPort;
import microservice_traceability.traceability.domain.spi.IOrderStatusHistoryPersistencePort;
import microservice_traceability.traceability.domain.usecase.OrderStatusHistoryUseCasePort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public IOrderStatusHistoryServicePort orderStatusHistoryServicePort (
            IOrderStatusHistoryPersistencePort orderStatusHistoryPersistencePort,
            IAuthenticationSecurityPort authenticationSecurityPort){
        return new OrderStatusHistoryUseCasePort(
                orderStatusHistoryPersistencePort,
                authenticationSecurityPort);
    }
}
