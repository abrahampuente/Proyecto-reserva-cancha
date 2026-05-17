package cl.duoc.mantenimientoservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MantenimientoRequest {

    @NotNull(message = "El canchaId es obligatorio")
    private Long canchaId;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;

    @NotBlank(message = "El técnico es obligatorio")
    private String tecnico;
}