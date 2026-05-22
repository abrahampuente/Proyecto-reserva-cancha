package cl.duoc.precioservice.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PrecioResponse {

    private Long id;
    private Long canchaId;
    private BigDecimal valor;
    private String moneda;
    private String descripcion;
    private String estado;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}