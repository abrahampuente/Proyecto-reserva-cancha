package cl.duoc.resenaservice.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResenaResponse {

    private Long id;
    private Long usuarioId;
    private Long canchaId;
    private String comentario;
    private Integer calificacion;
    private LocalDateTime fechaCreacion;
}