package synrgy.team4.backend.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RegisterDTO {
    @Schema(example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
    private UUID id;

    @Schema(example = "User 1")
    private String name;

    @Schema(example = "user1@gmail.com")
    private String email;
}
