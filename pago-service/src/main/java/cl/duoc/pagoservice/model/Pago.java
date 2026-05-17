package cl.duoc.pagoservice.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "pagos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long reservaId;

    @Column(nullable = false)
    private BigDecimal monto;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private String metodoPago;
}