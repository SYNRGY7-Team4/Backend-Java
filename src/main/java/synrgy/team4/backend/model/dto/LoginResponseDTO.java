package synrgy.team4.backend.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class LoginResponseDTO {
    @Schema(example = "97022c6c-56b5-4498-9d3d-f7fb7a1776b6")
    private UUID id;

    @Schema(example = "User1")
    private String name;

    @Schema(example = "user1@gmail.com")
    private String email;

    @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    public LoginResponseDTO(UUID id, String name, String email, String token) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.token = token;
    }
}
