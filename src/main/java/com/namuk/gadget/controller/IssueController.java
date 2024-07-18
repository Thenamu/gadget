package com.namuk.gadget.controller;

import com.namuk.gadget.domain.RefreshTokens;
import com.namuk.gadget.repository.token.RefreshTokenRepository;
import com.namuk.gadget.security.jwt.JwtIssuer;
import com.namuk.gadget.security.jwt.JwtPrincipal;
import com.namuk.gadget.security.jwt.JwtRefreshIssuer;
import com.namuk.gadget.security.userDetails.UserPrincipal;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class IssueController {

    private final JwtIssuer jwtIssuer;

    private final JwtRefreshIssuer jwtRefreshIssuer;

    private final RefreshTokenRepository refreshTokenRepository;

    @PostMapping(value = "/reissue/refresh")
    public ResponseEntity<?> refreshTokenIssuer(HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal JwtPrincipal jwtPrincipal) {
        String refreshToken = jwtRefreshIssuer.getRefreshTokenFromCookie(request);
        ResponseEntity<?> responseEntity = jwtRefreshIssuer.validateRefreshToken(refreshToken);
        System.out.println("responseEntity.getBody() = " + responseEntity.getBody());
        RefreshTokens byRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken);

        if(byRefreshToken == null) {
            return new ResponseEntity<>("Invalid refreshToken", HttpStatus.BAD_REQUEST);
        }

        String userId = jwtIssuer.extractId(refreshToken).orElseThrow(() -> new IllegalArgumentException("Invalid token"));
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = jwtIssuer.extractAuthorities(refreshToken).orElseThrow(() -> new IllegalArgumentException("Invalid token"));
        SimpleGrantedAuthority grantedAuthority = simpleGrantedAuthorities.get(0);
        String role = grantedAuthority.getAuthority();

        System.out.println("role = " + role);

        String newAccessToken = jwtIssuer.createAccessToken(userId, role);
        String newRefreshToken = jwtRefreshIssuer.createRefreshToken(userId, role);

        refreshTokenRepository.deleteByRefreshToken(refreshToken);
        jwtRefreshIssuer.storeRefreshToken(userId, newRefreshToken);

        response.setHeader("newAccessToken", newAccessToken);
        response.addCookie(createCookie(newRefreshToken));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Cookie createCookie(String value) {
        Cookie cookie = new Cookie("NewRefreshToken", value);
        cookie.setMaxAge(60 * 60);
        cookie.setHttpOnly(true);
        System.out.println("cookie_value = " + cookie.getValue());
        return cookie;
    }
}
