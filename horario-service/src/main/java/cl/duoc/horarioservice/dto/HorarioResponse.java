package cl.duoc.horarioservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HorarioResponse {

    private Long id;

    private Long canchaId;

    private String dayOfWeek;

    private LocalTime startTime;

    private LocalTime endTime;

    private Boolean available;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}