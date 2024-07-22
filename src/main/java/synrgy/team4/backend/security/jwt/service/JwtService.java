package synrgy.team4.backend.security.jwt.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import synrgy.team4.backend.model.entity.User;

@Service
public class JwtService {
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(User user) {
        return generateToken(new HashMap<>(), user);
    }

    public String generateToken(Map<String, Object> extraClaims, User user) {
        return buildToken(extraClaims, user, jwtExpiration);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            User user,
            long expiration
    ) {
        Date now = new Date();
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getEmail())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails user) {
        try {
            String email = extractEmail(token);

            extractAllClaims(token).getSubject();

            if (email.equals(user.getUsername())) {
                return true;
            }

        } catch (MalformedJwtException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        } catch (ExpiredJwtException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token has expired");
        } catch (UnsupportedJwtException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unsupported token");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token is empty");
        }
        return false;
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}