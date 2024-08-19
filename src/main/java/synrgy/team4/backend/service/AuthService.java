package synrgy.team4.backend.service;

import synrgy.team4.backend.model.dto.request.*;
import synrgy.team4.backend.model.dto.response.LoginResponse;
import synrgy.team4.backend.model.dto.response.UserResponse;

public interface AuthService {
    UserResponse register(RegisterUserRequest request);
    LoginResponse login(LoginRequest request);
    void checkEmail(CheckEmailRequest request);
    void checkPhoneNumber(CheckPhoneRequest request);
    void checkKTP(CheckKTPRequest request);
}
