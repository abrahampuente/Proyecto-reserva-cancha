package cl.duoc.userservice.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    private Long id;

    private String address;
    private String city;
    private String commune;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;
}