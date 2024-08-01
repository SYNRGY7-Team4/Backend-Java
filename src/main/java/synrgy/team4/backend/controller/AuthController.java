package synrgy.team4.backend.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
@Validated
public class AuthController {

    private final AuthService authService;
    private final TokenService tokenService;

    @Autowired
    public AuthController(AuthService authService, TokenService tokenService) {
        this.authService = authService;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<UserResponse>> register(
            @Valid @RequestBody RegisterUserRequest request
    ) {
        try {
            UserResponse userResponse = authService.register(request);

            BaseResponse<UserResponse> response = BaseResponse.<UserResponse>builder()
                    .success(true)
                    .data(userResponse)
                    .message("User registered successfully.")
                    .build();

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            BaseResponse<UserResponse> errorResponse = BaseResponse.<UserResponse>builder()
                    .success(false)
                    .data(null)
                    .message("Registration failed: " + e.getMessage())
                    .build();

            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        LoginResponse loginResponse = authService.login(request);

        BaseResponse<LoginResponse> response = BaseResponse.<LoginResponse>builder()
                .success(true)
                .data(loginResponse)
                .message("User Login successfully.")
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<BaseResponse<String>> logout() {
        BaseResponse<String> response = BaseResponse.<String>builder()
                .success(true)
                .data("Logout successfully.")
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
