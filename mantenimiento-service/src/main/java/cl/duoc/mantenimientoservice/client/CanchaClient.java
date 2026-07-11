package cl.duoc.mantenimientoservice.client;

import cl.duoc.mantenimientoservice.exception.BusinessRuleException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class CanchaClient {

    private final RestClient restClient;

    public CanchaClient(
            @Value("${services.cancha-service.url:http://localhost:8083}") String canchaServiceUrl
    ) {
        this.restClient = RestClient.builder()
                .baseUrl(canchaServiceUrl)
                .build();
    }

    public void validateCanchaExists(Long canchaId) {
        if (canchaId == null) {
            throw new BusinessRuleException("La cancha es obligatoria para registrar mantenimiento");
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