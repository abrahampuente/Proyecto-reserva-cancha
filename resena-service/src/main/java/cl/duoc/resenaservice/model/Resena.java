package cl.duoc.resenaservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "resenas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @NotNull
    @Column(name = "cancha_id", nullable = false)
    private Long canchaId;

    @NotBlank
    @Column(nullable = false)
    private String comentario;

    @Min(1) @Max(5)
    @Column(nullable = false)
    private Integer calificacion;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
    }
}