package cl.duoc.pagoservice.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    private String moneda;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private String metodoPago;

    private String codigoTransaccion;

    @Column(nullable = false)
    private LocalDateTime fechaPago;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;

        if (this.fechaPago == null) {
            this.fechaPago = now;
        }

        if (this.moneda == null) {
            this.moneda = "CLP";
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}