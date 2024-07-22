package synrgy.team4.backend.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RefreshTokenRequest {
    @JsonProperty("refresh_token")
    @NotEmpty(message = "Refresh token cannot be empty")
    private String refreshToken;
}
