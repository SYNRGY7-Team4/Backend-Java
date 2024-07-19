package synrgy.team4.backend.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    @Schema(example = "97022c6c-56b5-4498-9d3d-f7fb7a1776b6")
    private UUID id;

    @Schema(example = "User1")
    private String name;

    @Schema(example = "user1@gmail.com")
    private String email;

    @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
}
