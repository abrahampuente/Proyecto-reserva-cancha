package cl.duoc.canchaservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.List;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "canchas")
public class Cancha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la cancha es obligatorio")
    private String name;

    @NotBlank(message = "El tipo de deporte es obligatorio")
    private String sportType;

    @NotBlank(message = "El tipo de superficie es obligatorio")
    private String surfaceType;

    @NotNull(message = "La capacidad es obligatoria")
    private Integer capacity;

    @NotNull(message = "El ID del recinto es obligatorio")
    private Long recintoId;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "cancha", cascade = CascadeType.ALL)
    private List<CaracteristicaCancha> caracteristicas;
}