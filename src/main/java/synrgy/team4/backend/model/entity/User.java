package synrgy.team4.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class User extends BaseModel implements UserDetails {
    @Schema(example = "User1")
    private String name;

    @Schema(example = "user1@gmail.com")
    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "no_ktp", unique = true)
    private String noKTP;

    @Column(name = "no_hp", unique = true)
    private String noHP;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Lob
    @Column(name = "ektp_photo")
    private byte[] ektpPhoto;

    @Schema(example = "passworduser1")
    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Account> accounts;

    @Column(length = 100, nullable = true)
    private String otp;
    private Date otpExpiredDate;

    @Override
    public String getUsername() {
        return email;
    }
    public String getOtp() {
        return otp;
    }
    public void setOtp(String otp) {
        this.otp = otp;
    }
    public Date getOtpExpiredDate() {
        return otpExpiredDate;
    }
}
