package cl.duoc.canchaservice.client;

import cl.duoc.canchaservice.exception.BusinessRuleException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class RecintoClient {

    private final RestClient restClient;

    public RecintoClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl("http://RECINTO-SERVICE")
                .build();
    }

    public void validateRecintoExists(Long recintoId) {
        if (recintoId == null) {
            throw new BusinessRuleException("El recinto es obligatorio");
        }

        String response = restClient.get()
                .uri("/api/recintos/{id}", recintoId)
                .retrieve()
                .body(String.class);

        if (response == null) {
            throw new BusinessRuleException("El recinto no existe");
        }
    }
}