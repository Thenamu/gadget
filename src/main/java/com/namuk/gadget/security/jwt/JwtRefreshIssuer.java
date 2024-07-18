package com.namuk.gadget.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.namuk.gadget.domain.RefreshTokens;
import com.namuk.gadget.repository.token.RefreshTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@Transactional
@PropertySource("classpath:application-jwt.yml")
@RequiredArgsConstructor
public class JwtRefreshIssuer {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationInSeconds;

    @Value("${jwt.access.header}")
    private String accessHeader;

    private static final String REFRESH_TOKEN_SUBJECT = "refreshToken";

    private final JwtIssuer jwtIssuer;

    private final RefreshTokenRepository refreshTokenRepository;

    public String createRefreshToken(String id, String role) {
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withClaim("id", id)
                .withClaim("role", role)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenExpirationInSeconds * 1000))
                .sign(Algorithm.HMAC512(secretKey));
    }


    public String getRefreshTokenFromCookie(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();

        if(cookies != null) {
            for (Cookie cookie : cookies) {
                try {
                    if ("refreshToken".equals(cookie.getName())) {
                        return cookie.getValue();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public ResponseEntity<?> validateRefreshToken(String refreshToken) {

        if(refreshToken == null) {
            return new ResponseEntity<>("refreshToken is null", HttpStatus.BAD_REQUEST);
        }

        try {
            Optional<String> expireToken = jwtIssuer.isExpireToken(refreshToken);

            if(expireToken.isEmpty()) {
                return new ResponseEntity<>("refreshToken expired", HttpStatus.BAD_REQUEST);
            }

            String subjectClaim = jwtIssuer.extractSubjectClaim(refreshToken).orElseThrow();

            if(!"refreshToken".equals(subjectClaim)) {
                return new ResponseEntity<>("invalid refreshToken", HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>("refreshToken is valid and not expired yet", HttpStatus.OK);

        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while validating the token", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void storeRefreshToken(String id, String refreshToken) {

        Date expirationDate = new Date(System.currentTimeMillis() + refreshTokenExpirationInSeconds * 1000);

        RefreshTokens refreshTokens = RefreshTokens.builder()
                    .refreshUserId(id)
                    .refreshToken(refreshToken)
                    .expiration(String.valueOf(expirationDate))
                    .build();

        try {
            refreshTokenRepository.save(refreshTokens);
            System.out.println("refreshToken saved successfully for user ID: " + refreshTokens.getRefreshUserId());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to save refreshToken for user ID: " + refreshTokens.getRefreshUserId());
        }
    }
}
