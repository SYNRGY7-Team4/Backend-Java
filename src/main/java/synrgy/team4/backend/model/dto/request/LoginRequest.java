package synrgy.team4.backend.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = "Email cannot be empty")
    @Schema(example = "user1@gmail.com")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Schema(example = "passworduser1")
    private String password;
}