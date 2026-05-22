package cl.duoc.notificacionservice.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificacionResponse {

    private Long id;
    private String titulo;
    private String mensaje;
    private String destinatario;
    private String tipo;
    private String estado;
    private Boolean leida;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}