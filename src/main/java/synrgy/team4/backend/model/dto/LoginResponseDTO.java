package synrgy.team4.backend.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class LoginResponseDTO {
    private UUID id;
    private String name;
    private String email;
    private String token;

    public LoginResponseDTO(UUID id, String name, String email, String token) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.token = token;
    }
}
