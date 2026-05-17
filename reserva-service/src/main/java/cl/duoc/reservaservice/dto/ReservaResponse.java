package cl.duoc.reservaservice.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ReservaResponse {
    private Long id;
    private Long usuarioId;
    private Long canchaId;
    private Long horarioId;
    private LocalDateTime fechaReserva;
    private String estado;
}