package synrgy.team4.backend.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
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

//    @NotEmpty(message = "Gender cannot be empty")
//    private String gender;


//    @NotEmpty(message = "Place of birth cannot be empty")
//    @JsonProperty("place_of_birth")
//    private String placeOfBirth;

    @NotEmpty(message = "PIN cannot be empty")
    @Max(value = 6, message = "PIN must be 6 digits")
    @JsonProperty("pin")
    private String pin;

}
