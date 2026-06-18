package cl.duoc.reservaservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class ReservaRequest {

    @Schema(description = "ID del usuario que realiza la reserva", example = "1")
    @NotNull(message = "El usuarioId es obligatorio")
    private Long usuarioId;

    @Schema(description = "ID de la cancha reservada", example = "2")
    @NotNull(message = "El canchaId es obligatorio")
    private Long canchaId;

    @Schema(description = "ID del horario seleccionado", example = "3")
    @NotNull(message = "El horarioId es obligatorio")
    private Long horarioId;

    @Schema(
            description = "Fecha y hora de la reserva",
            example = "2026-07-15T18:00:00"
    )
    @NotNull(message = "La fechaReserva es obligatoria")
    private LocalDateTime fechaReserva;

    @Schema(
            description = "Estado de la reserva",
            example = "PENDIENTE"
    )
    private String estado;
}