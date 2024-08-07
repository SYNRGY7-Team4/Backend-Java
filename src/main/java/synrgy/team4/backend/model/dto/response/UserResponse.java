package synrgy.team4.backend.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Data
public class UserResponse {
    private String name;

    private String email;

    @JsonProperty("no_ktp")
    private String noKTP;

    @JsonProperty("no_hp")
    private String noHP;

    @JsonProperty("date_of_birth")
    private String dateOfBirth;

    @JsonProperty("account_number")
    private String accountNumber;

    @JsonProperty("account_pin")
    private String accountPin;

    @JsonProperty("ektp_photo")
    private String ektpPhoto;
}
