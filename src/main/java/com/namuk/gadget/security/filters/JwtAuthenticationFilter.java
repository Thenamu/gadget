package com.namuk.gadget.security.filters;

import com.namuk.gadget.repository.token.RefreshTokenRepository;
import com.namuk.gadget.security.jwt.JwtIssuer;
import com.namuk.gadget.security.jwt.JwtPrincipal;
import com.namuk.gadget.security.jwt.JwtRefreshIssuer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final JwtIssuer jwtIssuer;

    private final JwtRefreshIssuer jwtRefreshIssuer;

    @Autowired
    public JwtAuthenticationFilter(@Qualifier("JwtAuthenticationManager") AuthenticationManager authenticationManager,
                                   JwtIssuer jwtIssuer, JwtRefreshIssuer jwtRefreshIssuer) {
        this.authenticationManager = authenticationManager;
        this.jwtIssuer = jwtIssuer;
        this.jwtRefreshIssuer = jwtRefreshIssuer;
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // request에서 username과 password를 가져옴
        String id = obtainUsername(request); // request.getParameter(getUsernameParameter());
        String password = obtainPassword(request); // request.getParameter(getPasswordParameter());

        // DTO 객체 같이 AuthenticationManager에 넘길 usernamePasswordAuthenticationToken을 username과 password로 생성
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(id, password);

        // 부모 클래스의 getAuthenticationManager() 메소드를 호출하여 AuthenticationManager 인스턴스를 가져와 인증 시도
        // 의존성 주입(Dependency Injection)으로도 해결가능
        return super.getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken); // this, super를 붙여도 되고 안 붙여도 된다.
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

        JwtPrincipal principal = (JwtPrincipal) authentication.getPrincipal();
        String id = principal.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        GrantedAuthority authority = authorities.iterator().next(); // 첫 번째 권한만 추출
        String role = authority.getAuthority();

        String accessToken = jwtIssuer.createAccessToken(id, role);
        String refreshToken = jwtRefreshIssuer.createRefreshToken(id, role);
        System.out.println("Login Success");
        System.out.println("authentication = " + authentication);
        System.out.println("authentication = " + authentication.getPrincipal());
        System.out.println("accessToken = " + accessToken);
        System.out.println("refreshToken = " + refreshToken);

        jwtRefreshIssuer.storeRefreshToken(id, refreshToken);
        sendTokensToClient(response, accessToken, refreshToken);
        response.setStatus(HttpStatus.OK.value());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        System.out.println("Login failure" + " Login ID= " + response.getHeader(getUsernameParameter()));
        response.getWriter().print("Login failure" + " Login ID= " + response.getHeader(getUsernameParameter()));
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
    }

    public void sendTokensToClient(HttpServletResponse response, String accessToken, String refreshToken) {
        response.addHeader("Authorization", "Bearer " + accessToken);
        response.addCookie(createCookie(refreshToken));
    }

    private Cookie createCookie(String value) {
        Cookie cookie = new Cookie("refreshToken", value);
        cookie.setMaxAge(60 * 60);
        cookie.setHttpOnly(true);
        System.out.println("cookie_value = " + cookie.getValue());
        return cookie;
    }
}

