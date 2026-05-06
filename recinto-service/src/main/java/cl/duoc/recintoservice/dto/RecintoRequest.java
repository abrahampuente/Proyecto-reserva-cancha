package cl.duoc.recintoservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RecintoRequest {

    @NotBlank(message = "El nombre del recinto es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String name;

    @NotBlank(message = "La dirección es obligatoria")
    private String address;

    @NotBlank(message = "La ciudad es obligatoria")
    private String city;

    @NotBlank(message = "La comuna es obligatoria")
    private String commune;

    @NotBlank(message = "El teléfono es obligatorio")
    private String phone;

    private Long managerUserId;

    private List<ImagenRecintoRequest> imagenes;
}