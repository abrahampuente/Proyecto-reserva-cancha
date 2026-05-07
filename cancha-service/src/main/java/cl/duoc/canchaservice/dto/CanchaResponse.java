package cl.duoc.canchaservice.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CanchaResponse {

    private Long id;
    private String name;
    private String sportType;
    private String surfaceType;
    private Integer capacity;
    private Long recintoId;
    private String status;
    private List<String> caracteristicas;
}