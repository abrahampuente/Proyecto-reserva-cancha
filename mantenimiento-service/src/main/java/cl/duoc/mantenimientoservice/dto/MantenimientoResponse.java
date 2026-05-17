package cl.duoc.mantenimientoservice.dto;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MantenimientoResponse {

    private Long id;
    private Long canchaId;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;
    private String tecnico;
    private LocalDateTime fechaCreacion;
}