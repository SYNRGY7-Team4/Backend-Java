package synrgy.team4.backend.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserRequest {

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Email(message = "Email format is incorrect")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotBlank(message = "No KTP cannot be empty")
    @JsonProperty("no_ktp")
    private String noKTP;

    @NotBlank(message = "No Handphone cannot be empty")
    @JsonProperty("no_hp")
    private String noHP;

    @NotBlank(message = "Gender cannot be empty")
    private String gender;

    @NotBlank(message = "Date of birth cannot be empty")
    @JsonProperty("date_of_birth")
    private String dateOfBirth;

    @NotBlank(message = "Place of birth cannot be empty")
    @JsonProperty("place_of_birth")
    private String placeOfBirth;

    @NotBlank(message = "Password cannot be empty")
    private String password;
}
