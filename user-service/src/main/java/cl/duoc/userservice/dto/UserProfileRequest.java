package cl.duoc.userservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class UserProfileRequest {

    private String address;
    private String city;
    private String commune;
}