package cl.duoc.reservaservice.client;

import cl.duoc.reservaservice.exception.BusinessRuleException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class UserClient {

    private final RestClient restClient;

    public UserClient(
            @Qualifier("loadBalancedRestClientBuilder")
            RestClient.Builder restClientBuilder
    ) {
        this.restClient = restClientBuilder
                .baseUrl("http://USER-SERVICE")
                .build();
    }

    public void validateClienteExists(Long usuarioId) {
        if (usuarioId == null) {
            throw new BusinessRuleException("El usuario es obligatorio para crear una reserva");
        }

        Boolean exists = restClient.get()
                .uri("/api/users/{id}/exists", usuarioId)
                .retrieve()
                .body(Boolean.class);

        if (!Boolean.TRUE.equals(exists)) {
            throw new BusinessRuleException("El usuario indicado no existe");
        }

        String role = restClient.get()
                .uri("/api/users/{id}/role", usuarioId)
                .retrieve()
                .body(String.class);

        if (!"CLIENTE".equals(role) && !"ADMIN".equals(role)) {
            throw new BusinessRuleException("Solo un usuario CLIENTE o ADMIN puede crear reservas");
        }
    }
}