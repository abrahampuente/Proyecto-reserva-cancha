package cl.duoc.reservaservice.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
public class ReservaResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "1")
    private Long usuarioId;

    @Schema(example = "2")
    private Long canchaId;

    @Schema(example = "3")
    private Long horarioId;

    @Schema(example = "2026-07-15T18:00:00")
    private LocalDateTime fechaReserva;

    @Schema(example = "PENDIENTE")
    private String estado;

    @Schema(example = "2026-06-11T10:30:00")
    private LocalDateTime createdAt;

    @Schema(example = "2026-06-11T10:35:00")
    private LocalDateTime updatedAt;
}