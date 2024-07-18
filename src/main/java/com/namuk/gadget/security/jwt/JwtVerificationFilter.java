package com.namuk.gadget.security.jwt;

import com.namuk.gadget.domain.User;
import com.namuk.gadget.dto.member.JwtAuthenticationDTO;
import com.namuk.gadget.security.filters.JwtAuthenticationFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtVerificationFilter extends OncePerRequestFilter {

    private final JwtIssuer jwtIssuer;

    @Autowired
    public JwtVerificationFilter(JwtIssuer jwtIssuer) {
        this.jwtIssuer = jwtIssuer;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return !path.startsWith("/admin");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String pureToken = jwtIssuer.extractAccessTokenFromRequest(request);
        String extractedId = jwtIssuer.extractId(pureToken).orElseThrow(() -> new IllegalArgumentException("Invalid token"));
        // Long extractedNumber = jwtIssuer.extractNumber(pureToken).orElseThrow(() -> new IllegalArgumentException("Failed to extract number from token"));
        List<SimpleGrantedAuthority> authorities = jwtIssuer.extractAuthorities(pureToken).orElseThrow(() -> new IllegalArgumentException("Failed to extract authorities from the provided token."));


        /**
         * Token이 없으면 다음 Filter로 넘기고 Token이 있으면 유효성 검사 후 협의된 response 코드를 넘김
         */
        if(pureToken.isEmpty()) {
            System.out.println("AccessToken is missing or invalid = " + pureToken);
            // response.sendError(HttpServletResponse.SC_BAD_REQUEST, "AccessToken is missing or invalid");
            filterChain.doFilter(request, response);
            return;
        }

        if(jwtIssuer.isExpireToken(pureToken).isEmpty()) {
            System.out.println("AccessToken is expired");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            // filterChain.doFilter(request, response);
            return;
        }

        String subjectClaim = jwtIssuer.extractSubjectClaim(pureToken).orElseThrow();

        if(!subjectClaim.equals("AccessToken")) {
            System.out.println("Invalid AccessToken");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        JwtAuthenticationDTO jwtAuthDto = new JwtAuthenticationDTO();
        jwtAuthDto.setId(extractedId);
        jwtAuthDto.setRole(authorities);
        // jwtAuthDto.setNumber(extractedNumber);

        JwtPrincipal jwtPrincipal = new JwtPrincipal(jwtAuthDto.getId(), null, jwtAuthDto.getRole());

        Authentication authToken = new UsernamePasswordAuthenticationToken(jwtPrincipal, null, jwtPrincipal.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("principal = " + principal);
        filterChain.doFilter(request, response);
    }
}
