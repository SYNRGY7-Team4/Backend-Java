package synrgy.team4.backend.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CheckKTPRequest {
    @NotBlank(message = "KTP number cannot be empty")
    @Pattern(regexp = "\\d{16}", message = "KTP number must be exactly 16 digits")
    @JsonProperty("no_ktp")
    private String noKTP;
}
