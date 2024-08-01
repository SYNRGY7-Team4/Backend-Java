package synrgy.team4.backend.model.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Max;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserRequest {

    @Email(message = "Email format is incorrect")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @NotEmpty(message = "No Handphone cannot be empty")
    @JsonProperty("no_hp")
    private String noHP;

    @NotEmpty(message = "Password cannot be empty")
    private String password;

    @NotEmpty(message = "No KTP cannot be empty")
    @JsonProperty("no_ktp")
    private String noKTP;

    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @NotEmpty(message = "Date of birth cannot be empty")
    @JsonProperty("date_of_birth")
    private String dateOfBirth;

    @JsonProperty("ektp_photo")
    private String ektpPhoto;

    @NotEmpty(message = "PIN cannot be empty")
    @Max(value = 6, message = "PIN must be 6 digits")
    @JsonProperty("pin")
    private String pin;
}
