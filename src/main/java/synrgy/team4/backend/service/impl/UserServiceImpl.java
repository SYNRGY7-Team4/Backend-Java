package synrgy.team4.backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import synrgy.team4.backend.model.dto.response.UserResponse;
import synrgy.team4.backend.model.entity.User;
import synrgy.team4.backend.repository.UserRepository;
import synrgy.team4.backend.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse authenticatedUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .noKTP(user.getNoKTP())
                .noHP(user.getNoHP())
                .gender(user.getGender())
                .dateOfBirth(user.getDateOfBirth().toString())
                .placeOfBirth(user.getPlaceOfBirth())
                .build();
    }
}