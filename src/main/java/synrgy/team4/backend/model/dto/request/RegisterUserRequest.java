package synrgy.team4.backend.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserRequest {

    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @Email(message = "Email format is incorrect")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @NotEmpty(message = "No KTP cannot be empty")
    @JsonProperty("no_ktp")
    private String noKTP;

    @NotEmpty(message = "No Handphone cannot be empty")
    @JsonProperty("no_hp")
    private String noHP;

    @NotEmpty(message = "Gender cannot be empty")
    private String gender;

    @NotEmpty(message = "Date of birth cannot be empty")
    @JsonProperty("date_of_birth")
    private String dateOfBirth;

    @NotEmpty(message = "Place of birth cannot be empty")
    @JsonProperty("place_of_birth")
    private String placeOfBirth;

    @NotEmpty(message = "Password cannot be empty")
    private String password;
}
