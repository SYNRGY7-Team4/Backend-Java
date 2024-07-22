package synrgy.team4.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import synrgy.team4.backend.model.dto.Gender;

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
@SQLRestriction("deleted = false")
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

//    @Enumerated(EnumType.STRING)
//    @Column(name = "gender")
//    private Gender gender;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Column(name = "date_of_birth")
    private Date dateOfBirth;

//    @Column(name = "place_of_birth")
//    private String placeOfBirth;

    @Schema(example = "passworduser1")
    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return email;
    }
}