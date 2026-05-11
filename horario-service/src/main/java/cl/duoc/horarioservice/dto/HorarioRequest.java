package cl.duoc.horarioservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class HorarioRequest {

    @NotNull(message = "El id de la cancha es obligatorio")
    private Long canchaId;

    private String dayOfWeek;

    private LocalTime startTime;

    private LocalTime endTime;

    private Boolean available;

    private String status;
}