package cl.duoc.reservaservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReservaRequest {

    @NotNull(message = "El usuarioId es obligatorio")
    private Long usuarioId;

    @NotNull(message = "El canchaId es obligatorio")
    private Long canchaId;

    @NotNull(message = "El horarioId es obligatorio")
    private Long horarioId;

    @NotNull(message = "La fechaReserva es obligatoria")
    private LocalDateTime fechaReserva;

    @NotNull(message = "El estado es obligatorio")
    private String estado;
}