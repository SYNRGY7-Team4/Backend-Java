package synrgy.team4.backend.model.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Max;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserRequest {

    @Email(message = "Format email salah")
    @NotEmpty(message = "Email tidak boleh kosong")
    private String email;

    @NotEmpty(message = "No Handphone tidak boleh kosong")
    @JsonProperty("no_hp")
    private String noHP;

    @NotEmpty(message = "Password tidak boleh kosong")
    private String password;

    @NotEmpty(message = "No KTP tidak boleh kosong")
    @JsonProperty("no_ktp")
    private String noKTP;

    @NotEmpty(message = "Nama tidak boleh kosong")
    private String name;

    @NotEmpty(message = "Tanggal lahir tidak boleh kosong")
    @JsonProperty("date_of_birth")
    private String dateOfBirth;

    @JsonProperty("ektp_photo")
    private String ektpPhoto;

    @NotEmpty(message = "PIN tidak boleh kosong")
    @Max(value = 6, message = "PIN harus 6 digit")
    @JsonProperty("pin")
    private String pin;
}
