package synrgy.team4.backend.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotEmpty(message = "Email cannot be empty")
    @Schema(example = "user1@gmail.com")
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    @Schema(example = "passworduser1")
    private String password;
}