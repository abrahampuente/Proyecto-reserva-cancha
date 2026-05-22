package cl.duoc.pagoservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class PagoRequest {

    @NotNull(message = "El reservaId es obligatorio")
    private Long reservaId;

    @NotNull(message = "El monto es obligatorio")
    private BigDecimal monto;

    @NotNull(message = "El metodo de pago es obligatorio")
    private String metodoPago;

    private String estado;

    private String codigoTransaccion;
}