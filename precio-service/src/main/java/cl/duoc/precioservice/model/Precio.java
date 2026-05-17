package cl.duoc.precioservice.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "precios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Precio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long canchaId;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private String descripcion;
}