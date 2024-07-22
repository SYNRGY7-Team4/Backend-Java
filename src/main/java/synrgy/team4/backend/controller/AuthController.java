package synrgy.team4.backend.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class AuthController {

    private final AuthService authService;
    private final TokenService tokenService;

    @Autowired
    public AuthController(AuthService authService, TokenService tokenService) {
        this.authService = authService;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<UserResponse>> register(@Valid @RequestBody RegisterUserRequest request) {
        UserResponse userResponse = authService.register(request);

        BaseResponse<UserResponse> response = BaseResponse.<UserResponse>builder()
                .success(true)
                .data(userResponse)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
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
