package cl.duoc.reservaservice.client;

import cl.duoc.reservaservice.exception.BusinessRuleException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class HorarioClient {

    private final RestClient restClient;

    public HorarioClient(
            @Qualifier("loadBalancedRestClientBuilder")
            RestClient.Builder restClientBuilder
    ) {
        this.restClient = restClientBuilder
                .baseUrl("http://HORARIO-SERVICE")
                .build();
    }

    public void validateHorarioExists(Long horarioId) {
        if (horarioId == null) {
            throw new BusinessRuleException("El horario es obligatorio para crear una reserva");
        }

        Boolean exists = restClient.get()
                .uri("/api/horarios/{id}/exists", horarioId)
                .retrieve()
                .body(Boolean.class);

        if (!Boolean.TRUE.equals(exists)) {
            throw new BusinessRuleException("El horario indicado no existe");
        }
    }
}