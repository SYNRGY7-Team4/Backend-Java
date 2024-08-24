package synrgy.team4.backend.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class VerifyOtpRequest {

    @Schema(description = "Email user", example = "affudina663@gmail.com")
    private String email;

    @Schema(description = "OTP Already Send", example = "881312")
    private String otp;
}
