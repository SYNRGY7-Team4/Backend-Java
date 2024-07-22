package synrgy.team4.backend.service;

import synrgy.team4.backend.model.dto.request.RefreshTokenRequest;
import synrgy.team4.backend.model.dto.response.LoginResponse;
import synrgy.team4.backend.model.entity.Token;

public interface TokenService {
    Token createToken(String email, String jwtToken, String refreshToken);
    String createRefreshToken();
    Token verifyExpirationRefreshToken(Token token);
    LoginResponse refreshToken(RefreshTokenRequest refreshToken);
}
