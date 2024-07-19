package synrgy.team4.backend.service;

import synrgy.team4.backend.model.dto.request.LoginRequest;
import synrgy.team4.backend.model.dto.request.RegisterUserRequest;
import synrgy.team4.backend.model.dto.response.LoginResponse;
import synrgy.team4.backend.model.dto.response.UserResponse;

public interface AuthService {
    UserResponse register(RegisterUserRequest request);
    LoginResponse login(LoginRequest request);
}
