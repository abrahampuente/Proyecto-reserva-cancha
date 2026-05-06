package cl.duoc.recintoservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ImagenRecintoRequest {

    private String imageUrl;
    private String description;
}