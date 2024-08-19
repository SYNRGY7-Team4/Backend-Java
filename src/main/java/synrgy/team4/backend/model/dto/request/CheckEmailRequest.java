package synrgy.team4.backend.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckEmailRequest {
    @Email(message = "Email format is incorrect")
    @NotBlank(message = "Email cannot be empty")
    private String email;
}
