package cl.duoc.precioservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class PrecioRequest {

    @NotNull(message = "El canchaId es obligatorio")
    private Long canchaId;

    @NotNull(message = "El valor es obligatorio")
    private BigDecimal valor;

    @NotNull(message = "La descripcion es obligatoria")
    private String descripcion;

    private String estado;
}