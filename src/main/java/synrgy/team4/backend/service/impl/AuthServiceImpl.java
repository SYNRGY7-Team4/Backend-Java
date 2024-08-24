package synrgy.team4.backend.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import synrgy.team4.backend.model.dto.request.*;
import synrgy.team4.backend.model.dto.response.LoginResponse;
import synrgy.team4.backend.model.dto.response.UserResponse;
import synrgy.team4.backend.model.entity.Account;
import synrgy.team4.backend.model.entity.User;
import synrgy.team4.backend.repository.AccountRepository;
import synrgy.team4.backend.repository.UserRepository;
import synrgy.team4.backend.security.jwt.service.JwtService;
import synrgy.team4.backend.service.AuthService;
import synrgy.team4.backend.service.OtpService;
import synrgy.team4.backend.service.TokenService;
import synrgy.team4.backend.utils.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenService tokenService;
    private final EmailSender emailSender;
    private final OtpService otpService;
    private final Map<String, RegisterUserRequest> temporaryUserDataStorage = new HashMap<>();


    @Autowired
    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            TokenService tokenService,
            AccountRepository accountRepository,
            EmailSender emailSender,
            OtpService otpService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.tokenService = tokenService;
        this.accountRepository = accountRepository;
        this.emailSender = emailSender;
        this.otpService = otpService;
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
        // Ambil data sementara berdasarkan email
        RegisterUserRequest tempRequest = otpService.getTemporaryUserData(request.getEmail());

        // Validasi apakah data ada dan OTP sudah diverifikasi
        if (tempRequest == null || !tempRequest.isVerified()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP not verified or data not found.");
        }
        System.out.println("Data found and OTP verified for: " + tempRequest.getEmail());
        // Validasi jika email, noHP, atau noKTP sudah ada di database
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
        }
        if (userRepository.existsByNoHP(request.getNoHP())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number already registered");
        }
        if (userRepository.existsByNoKTP(request.getNoKTP())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "KTP number already registered");
        }
        if (accountRepository.existsByAccountNumber(AccountNumberGenerator.generateAccountNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account number already registered");
        }
        otpService.removeTemporaryUserData(request.getEmail());
        // Parsing DOB
        Date dateOfBirth = ValidateDate.parseDate(request.getDateOfBirth());

        // Build and save the new User entity
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .noKTP(request.getNoKTP())
                .noHP(request.getNoHP())
                .dateOfBirth(dateOfBirth)
                .ektpPhoto(request.getEktpPhoto())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

        // Build and save the new Account entity
        Account account = Account.builder()
                .accountNumber(AccountNumberGenerator.generateAccountNumber())
                .balance(BigDecimal.valueOf(0.0))
                .pin(PinHashing.hashPin(request.getPin()))
                .user(user)
                .build();

        accountRepository.save(account);

        // Setelah berhasil registrasi, hapus data sementara untuk email tersebut
        otpService.removeTemporaryUserData(request.getEmail());

        return UserResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .noKTP(user.getNoKTP())
                .noHP(user.getNoHP())
                .dateOfBirth(user.getDateOfBirth().toString())
                .ektpPhoto(user.getEktpPhoto())
                .accountNumber(account.getAccountNumber())
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
        // Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

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

        String refreshToken = tokenService.createRefreshToken();

        tokenService.createToken(user.getEmail(), jwtToken, refreshToken);

        return LoginResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .jwtToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void checkEmail(CheckEmailRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
        }
    }

    @Override
    public void checkPhoneNumber(CheckPhoneRequest request) {
        if (userRepository.existsByNoHP(request.getNoHP())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No HP already registered");
        }
    }

    @Override
    public void checkKTP(CheckKTPRequest request) {
        if (userRepository.existsByNoKTP(request.getNoKTP())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No KTP already registered");
        }
    }
}
