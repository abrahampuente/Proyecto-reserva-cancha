package cl.duoc.recintoservice.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecintoResponse {

    private Long id;
    private String name;
    private String address;
    private String city;
    private String commune;
    private String phone;
    private Long managerUserId;
    private String status;
    private List<String> imagenes;
}