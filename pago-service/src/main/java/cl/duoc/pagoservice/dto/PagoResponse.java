package cl.duoc.pagoservice.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PagoResponse {

    private Long id;
    private Long reservaId;
    private BigDecimal monto;
    private String moneda;
    private String estado;
    private String metodoPago;
    private String codigoTransaccion;
    private LocalDateTime fechaPago;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}