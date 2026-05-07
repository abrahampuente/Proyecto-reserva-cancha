package cl.duoc.canchaservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CanchaRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "El tipo de deporte es obligatorio")
    private String sportType;

    @NotBlank(message = "El tipo de superficie es obligatorio")
    private String surfaceType;

    @NotNull(message = "La capacidad es obligatoria")
    private Integer capacity;

    @NotNull(message = "El ID del recinto es obligatorio")
    private Long recintoId;

    private List<CaracteristicaCanchaRequest> caracteristicas;
}