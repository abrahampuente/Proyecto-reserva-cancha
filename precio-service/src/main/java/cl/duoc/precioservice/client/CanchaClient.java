package cl.duoc.precioservice.client;

import cl.duoc.precioservice.exception.BusinessRuleException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class CanchaClient {

    private final RestClient restClient;

    public CanchaClient(
            @Qualifier("loadBalancedRestClientBuilder")
            RestClient.Builder restClientBuilder
    ) {
        this.restClient = restClientBuilder
                .baseUrl("http://CANCHA-SERVICE")
                .build();
    }
    public void validateCanchaExists(Long canchaId) {
        if (canchaId == null) {
            throw new BusinessRuleException("La cancha es obligatoria para crear un precio");
        }

        Boolean exists = restClient.get()
                .uri("/api/canchas/{id}/exists", canchaId)
                .retrieve()
                .body(Boolean.class);

        if (!Boolean.TRUE.equals(exists)) {
            throw new BusinessRuleException("La cancha indicada no existe");
        }
    }
}