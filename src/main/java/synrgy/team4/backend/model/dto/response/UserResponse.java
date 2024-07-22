package synrgy.team4.backend.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import synrgy.team4.backend.model.dto.Gender;

import java.util.UUID;

@Builder
@Data
public class UserResponse {
    private UUID id;
    private String name;
    private String email;
    @JsonProperty("no_ktp")
    private String noKTP;
    @JsonProperty("no_hp")
    private String noHP;
//    private Gender gender;
    @JsonProperty("date_of_birth")
    private String dateOfBirth;
//    @JsonProperty("place_of_birth")
//    private String placeOfBirth;
}
