package cl.duoc.pagoservice.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class PagoResponse {
    private Long id;
    private Long reservaId;
    private BigDecimal monto;
    private String estado;
    private String metodoPago;
}