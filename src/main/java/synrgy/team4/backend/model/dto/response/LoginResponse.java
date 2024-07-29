package synrgy.team4.backend.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    @Schema(example = "User1")
    private String name;

    @Schema(example = "user1@gmail.com")
    private String email;

    @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    @JsonProperty("jwt_token")
    private String jwtToken;

    @JsonProperty("refresh_token")
    private String refreshToken;
}
