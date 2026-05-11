package cl.duoc.horarioservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "horarios")
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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