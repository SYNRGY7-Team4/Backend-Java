package synrgy.team4.backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import synrgy.team4.backend.model.dto.response.UserResponse;
import synrgy.team4.backend.model.entity.Account;
import synrgy.team4.backend.model.entity.User;
import synrgy.team4.backend.repository.AccountRepository;
import synrgy.team4.backend.repository.UserRepository;
import synrgy.team4.backend.security.jwt.CustomUserDetails;
import synrgy.team4.backend.service.UserService;

import java.util.Arrays;
import java.util.Base64;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository, AccountRepository accountRepository
    ) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
        return new CustomUserDetails(user); // Return CustomUserDetails instance
    }

    @Override
    public UserResponse getUserResponse() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Account account = accountRepository.findByUserId(userDetails.getId());

        return UserResponse.builder()
                .name(userDetails.getName())
                .email(userDetails.getEmail())
                .noKTP(userDetails.getNoKTP())
                .noHP(userDetails.getNoHP())
                .dateOfBirth(userDetails.getDateOfBirth().toString())
                .ektpPhoto(Arrays.toString(userDetails.getEktpPhoto()))
                .accountNumber(account.getAccountNumber())
                .build();
    }
}
