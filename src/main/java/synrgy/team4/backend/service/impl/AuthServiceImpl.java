package synrgy.team4.backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import synrgy.team4.backend.model.dto.Gender;
import synrgy.team4.backend.model.dto.request.LoginRequest;
import synrgy.team4.backend.model.dto.request.RegisterUserRequest;
import synrgy.team4.backend.model.dto.response.LoginResponse;
import synrgy.team4.backend.model.dto.response.UserResponse;
import synrgy.team4.backend.model.entity.User;
import synrgy.team4.backend.repository.UserRepository;
import synrgy.team4.backend.security.jwt.service.JwtService;
import synrgy.team4.backend.service.AuthService;
import synrgy.team4.backend.service.ValidationService;
import synrgy.team4.backend.utils.ValidateDate;

import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService {

    private final ValidationService validationService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public AuthServiceImpl(
            ValidationService validationService,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.validationService = validationService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    /**
     * Registers a new user in the system.
     *
     * @param request The registration request containing user details
     * @return UserResponse object containing the registered user's details
     * @throws ResponseStatusException if email, phone number, or KTP number already exists
     */
    @Override
    public UserResponse register(RegisterUserRequest request) {
        // Validate the request
        validationService.validate(request);

        // Check for existing email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
        }

        // Check for existing phone number
        if (userRepository.existsByNoHP(request.getNoHP())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No HP already registered");
        }

        // Check for existing KTP number
        if (userRepository.existsByNoKTP(request.getNoKTP())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No KTP already registered");
        }

        Date dateOfBirth = ValidateDate.parseDate(request.getDateOfBirth());

        // Build and save the new User entity
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .noKTP(request.getNoKTP())
                .noHP(request.getNoHP())
                .gender(Gender.valueOf(request.getGender().toUpperCase()))
                .dateOfBirth(dateOfBirth)
                .placeOfBirth(request.getPlaceOfBirth())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

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

    /**
     * Authenticates a user and generates a JWT token.
     *
     * @param request The login request containing user credentials
     * @return LoginResponse object containing user details and JWT token
     * @throws ResponseStatusException if user is not found or password is invalid
     */
    @Override
    public LoginResponse login(LoginRequest request) {
        // Validate the request
        validationService.validate(request);

        // Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        // Check if the password matches the one stored in the database
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
        }

        // Authenticate the user using Spring Security's AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Set the authentication in the SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token
        String jwtToken = jwtService.generateToken(user);

        return LoginResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .token(jwtToken)
                .build();
    }
}