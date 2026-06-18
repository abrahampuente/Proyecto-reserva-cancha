package cl.duoc.reservaservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI reservaOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Reserva Service API")
                        .description("Microservicio encargado de la gestión de reservas de canchas deportivas")
                        .version("1.0.0"))
                .addSecurityItem(new SecurityRequirement().addList("basicAuth"))
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        "basicAuth",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("basic")
                                )
                );
    }
}