package synrgy.team4.backend.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import synrgy.team4.backend.model.dto.*;
import synrgy.team4.backend.model.dto.request.LoginRequest;
import synrgy.team4.backend.model.dto.request.RegisterUserRequest;
import synrgy.team4.backend.model.dto.response.LoginResponse;
import synrgy.team4.backend.model.dto.response.UserResponse;
import synrgy.team4.backend.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        UserResponse userResponse = authService.register(request);
        return ApiResponse.<UserResponse>builder()
                .success(true)
                .data(userResponse)
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse loginResponse = authService.login(request);
        return ApiResponse.<LoginResponse>builder()
                .success(true)
                .data(loginResponse)
                .build();
    }
}
