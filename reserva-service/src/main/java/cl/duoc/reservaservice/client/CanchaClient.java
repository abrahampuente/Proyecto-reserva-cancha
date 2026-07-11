package cl.duoc.reservaservice.client;

import cl.duoc.reservaservice.exception.BusinessRuleException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class CanchaClient {

    private final RestClient restClient;

    public CanchaClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl("http://CANCHA-SERVICE")
                .build();
    }
    public void validateCanchaExists(Long canchaId) {
        if (canchaId == null) {
            throw new BusinessRuleException("La cancha es obligatoria para crear una reserva");
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