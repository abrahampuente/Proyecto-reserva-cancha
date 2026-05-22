package cl.duoc.pagoservice.client;

import cl.duoc.pagoservice.exception.BusinessRuleException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ReservaClient {

    private final RestClient restClient;

    public ReservaClient() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8085")
                .build();
    }

    public void validateReservaExists(Long reservaId) {
        if (reservaId == null) {
            throw new BusinessRuleException("La reserva es obligatoria para registrar un pago");
        }

        Boolean exists = restClient.get()
                .uri("/api/reservas/{id}/exists", reservaId)
                .retrieve()
                .body(Boolean.class);

        if (!Boolean.TRUE.equals(exists)) {
            throw new BusinessRuleException("La reserva indicada no existe");
        }
    }
}