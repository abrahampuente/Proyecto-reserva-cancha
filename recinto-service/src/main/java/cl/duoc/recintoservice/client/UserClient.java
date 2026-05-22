package cl.duoc.recintoservice.client;

import cl.duoc.recintoservice.exception.BusinessRuleException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class UserClient {

    private final RestClient restClient;

    public UserClient() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8081")
                .build();
    }

    public boolean existsById(Long userId) {
        Boolean exists = restClient.get()
                .uri("/api/users/{id}/exists", userId)
                .retrieve()
                .body(Boolean.class);

        return Boolean.TRUE.equals(exists);
    }

    public String getRoleById(Long userId) {
        return restClient.get()
                .uri("/api/users/{id}/role", userId)
                .retrieve()
                .body(String.class);
    }

    public void validateOwnerOrAdmin(Long userId) {
        if (userId == null) {
            throw new BusinessRuleException("El dueño del recinto es obligatorio");
        }

        if (!existsById(userId)) {
            throw new BusinessRuleException("El usuario dueño no existe");
        }

        String role = getRoleById(userId);

        if (!"DUENIO".equals(role) && !"ADMIN".equals(role)) {
            throw new BusinessRuleException("Solo un usuario DUENIO o ADMIN puede registrar recintos");
        }
    }
}
