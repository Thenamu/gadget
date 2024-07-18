package com.namuk.gadget.security.filters;

import com.namuk.gadget.domain.RefreshTokens;
import com.namuk.gadget.repository.token.RefreshTokenRepository;
import com.namuk.gadget.security.jwt.JwtRefreshIssuer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private static final String LOGOUT_URI = "/logout";

    private static final String METHOD = "POST";

    private final JwtRefreshIssuer jwtRefreshIssuer;

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

//        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        doFilter((HttpServletRequest) request, (HttpServletResponse) response, filterChain);

    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        String requestURI = request.getRequestURI();
        String requestMethod = request.getMethod();

        if(!LOGOUT_URI.equals(requestURI) || !METHOD.equals(requestMethod)) {
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = jwtRefreshIssuer.getRefreshTokenFromCookie(request);
        ResponseEntity<?> responseEntity = jwtRefreshIssuer.validateRefreshToken(refreshToken);
        System.out.println("responseEntity = " + responseEntity);

        RefreshTokens byRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken);

        if(byRefreshToken == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        refreshTokenRepository.deleteByRefreshToken(refreshToken);
        Cookie cookie = removeCookie();

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
        System.out.println("Cookies have been successfully deleted. " + cookie);
    }

    private Cookie removeCookie() {
        Cookie cookie = new Cookie("refreshToken", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");

        return cookie;
    }
}
