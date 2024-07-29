package synrgy.team4.backend.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import synrgy.team4.backend.model.dto.request.RefreshTokenRequest;
import synrgy.team4.backend.model.dto.response.LoginResponse;
import synrgy.team4.backend.model.entity.Token;
import synrgy.team4.backend.model.entity.User;
import synrgy.team4.backend.repository.TokenRepository;
import synrgy.team4.backend.repository.UserRepository;
import synrgy.team4.backend.security.jwt.service.JwtService;
import synrgy.team4.backend.service.TokenService;

import java.util.Date;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public TokenServiceImpl(
            TokenRepository tokenRepository,
            UserRepository userRepository,
            JwtService jwtService) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public Token createToken(String email, String jwtToken, String refreshToken) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Token token = Token.builder()
                .user(user)
                .jwtToken(jwtToken)
                .refreshToken(refreshToken)
                .expiryDateRefreshToken(new Date(new Date().getTime() + 600000))
                .build();

        return tokenRepository.save(token);
    }

    @Override
    public String createRefreshToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public Token verifyExpirationRefreshToken(Token token) {
        if (token.getExpiryDateRefreshToken().before(new Date())) {
            tokenRepository.delete(token);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token is expired. Please login again to get a new refresh token.");
        }
        return token;
    }

    @Override
    public LoginResponse refreshToken(RefreshTokenRequest request) {
        return tokenRepository.findByRefreshToken(request.getRefreshToken())
                .map(this::verifyExpirationRefreshToken)
                .map(Token::getUser)
                .map(user -> {
                    tokenRepository.deleteByRefreshToken(request.getRefreshToken());
                    String jwtToken = jwtService.generateToken(user);
                    String refreshToken = createRefreshToken();

                    Token token = createToken(user.getEmail(), jwtToken, refreshToken);

                    return LoginResponse.builder()
                            .name(user.getName())
                            .email(user.getEmail())
                            .jwtToken(token.getJwtToken())
                            .refreshToken(token.getRefreshToken())
                            .build();
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token"));
    }
}
