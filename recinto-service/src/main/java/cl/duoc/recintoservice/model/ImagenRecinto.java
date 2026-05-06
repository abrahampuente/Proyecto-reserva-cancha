package cl.duoc.recintoservice.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "imagenes_recinto")
public class ImagenRecinto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    private String description;

    @ManyToOne
    @JoinColumn(name = "recinto_id")
    private Recinto recinto;
}
