package synrgy.team4.backend.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {

    @Schema(example = "user1@gmail.com")
    private String email;

    @Schema(example = "passworduser1")
    private String password;
}