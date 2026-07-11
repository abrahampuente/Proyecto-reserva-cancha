package cl.duoc.recintoservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${API_GATEWAY_URL:http://localhost:8080}")
    private String apiGatewayUrl;

    @Bean
    public OpenAPI recintoServiceOpenAPI() {
        return new OpenAPI()
                .servers(List.of(
                        new Server()
                                .url(apiGatewayUrl)
                                .description("API Gateway")
                ))
                .info(new Info()
                        .title("Recinto Service API")
                        .version("1.0.0")
                        .description("Microservicio encargado de la gestión de recintos deportivos"));
    }
}