package synrgy.team4.backend.security.jwt;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import synrgy.team4.backend.model.entity.Account;
import synrgy.team4.backend.model.entity.User;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public String getNoKTP() {
        return user.getNoKTP();
    }

    public String getNoHP() {
        return user.getNoHP();
    }

    public Date getDateOfBirth() {
        return user.getDateOfBirth();
    }

    public UUID getId() {
        return user.getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getName() {
        return user.getName();
    }

    public User getUser() {
        return user;
    }

    public List<Account> getAccounts() {
        return user.getAccounts();
    }

    public byte[] getEktpPhoto() { return user.getEktpPhoto(); }
}
