package synrgy.team4.backend.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CheckPhoneRequest {
    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(regexp = "\\d{11,13}", message = "Phone number must be between 11 and 13 digits")
    @JsonProperty("no_hp")
    private String noHP;
}
