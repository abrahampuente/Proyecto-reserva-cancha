package cl.duoc.precioservice.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class PrecioResponse {
    private Long id;
    private Long canchaId;
    private BigDecimal valor;
    private String descripcion;
}