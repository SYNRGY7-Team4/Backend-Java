package synrgy.team4.backend.model.dto.request;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserRequest {

    @Email(message = "Email format is incorrect")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(regexp = "\\d{12}", message = "Phone number must be exactly 12 digits")
    @JsonProperty("no_hp")
    private String noHP;

    @NotBlank(message = "Password cannot be empty")
    private String password;

    @NotBlank(message = "KTP number cannot be empty")
    @Pattern(regexp = "\\d{16}", message = "KTP number must be exactly 16 digits")
    @JsonProperty("no_ktp")
    private String noKTP;

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Date of birth cannot be empty")
    @JsonProperty("date_of_birth")
    private String dateOfBirth;

    @JsonProperty("ektp_photo")
    private String ektpPhoto;

    @NotBlank(message = "PIN cannot be empty")
    @Pattern(regexp = "\\d{6}", message = "PIN must be exactly 6 digits")
    @JsonProperty("pin")
    private String pin;
}
