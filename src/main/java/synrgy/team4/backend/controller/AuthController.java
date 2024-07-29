package synrgy.team4.backend.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import synrgy.team4.backend.model.dto.request.LoginRequest;
import synrgy.team4.backend.model.dto.request.RefreshTokenRequest;
import synrgy.team4.backend.model.dto.request.RegisterUserRequest;
import synrgy.team4.backend.model.dto.response.BaseResponse;
import synrgy.team4.backend.model.dto.response.LoginResponse;
import synrgy.team4.backend.model.dto.response.UserResponse;
import synrgy.team4.backend.service.AuthService;
import synrgy.team4.backend.service.TokenService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final TokenService tokenService;

    @Autowired
    public AuthController(AuthService authService, TokenService tokenService) {
        this.authService = authService;
        this.tokenService = tokenService;
    }

    @PostMapping(value = "/register", consumes = {"multipart/form-data"})
    public ResponseEntity<BaseResponse<UserResponse>> register(
//            @Valid @RequestPart RegisterUserRequest request
            @RequestPart("email") @Valid String email,
            @RequestPart("no_hp") @Valid String noHP,
            @RequestPart("password") @Valid String password,
            @RequestPart("confirm_password") @Valid String confirmPassword,
            @RequestPart("no_ktp") @Valid String noKTP,
            @RequestPart("name") @Valid String name,
            @RequestPart("date_of_birth") @Valid String dateOfBirth,
            @RequestPart("ektp_photo") @Valid MultipartFile ektpPhoto,
            @RequestPart("pin") @Valid String pin,
            @RequestPart("confirm_pin") @Valid String confirmPin
    ) {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setEmail(email);
        request.setNoHP(noHP);
        request.setPassword(password);
        request.setConfirmPassword(confirmPassword);
        request.setNoKTP(noKTP);
        request.setName(name);
        request.setDateOfBirth(dateOfBirth);
        request.setEktpPhoto(ektpPhoto);
        request.setPin(pin);
        request.setConfirmPin(confirmPin);

        UserResponse userResponse = authService.register(request);

        BaseResponse<UserResponse> response = BaseResponse.<UserResponse>builder()
                .success(true)
                .data(userResponse)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping(value = "/login", consumes = "multipart/form-data")
    public ResponseEntity<BaseResponse<LoginResponse>> login(
//            @Valid @RequestBody LoginRequest request
            @RequestPart("email") @Valid String email,
            @RequestPart("password") @Valid String password
    ) {
        LoginRequest request = new LoginRequest();
        request.setEmail(email);
        request.setPassword(password);

        LoginResponse loginResponse = authService.login(request);

        BaseResponse<LoginResponse> response = BaseResponse.<LoginResponse>builder()
                .success(true)
                .data(loginResponse)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<BaseResponse<String>> logout() {
        BaseResponse<String> response = BaseResponse.<String>builder()
                .success(true)
                .data("Logout success")
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<BaseResponse<LoginResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        LoginResponse loginResponse = tokenService.refreshToken(request);

        BaseResponse<LoginResponse> response = BaseResponse.<LoginResponse>builder()
                .success(true)
                .data(loginResponse)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
