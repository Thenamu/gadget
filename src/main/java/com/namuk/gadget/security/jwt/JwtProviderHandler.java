//package com.namuk.gadget.controller.security.jwt;
//
//import com.namuk.gadget.controller.security.userDetails.UserPrincipal;
//import com.namuk.gadget.repository.member.UserRepository;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
//
//import java.io.IOException;
//
//@Slf4j
//@RequiredArgsConstructor
//public class JwtProviderHandler extends SimpleUrlAuthenticationSuccessHandler {
//
//    private final JwtIssuer jwtIssuer;
//
//    private final UserRepository userRepository;
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        String id = extractId(authentication);
//        String accessToken = jwtIssuer.createAccessToken(id);
//        String refreshToken = jwtIssuer.createRefreshToken();
//
//        jwtIssuer.sendAccessAndRefreshToken(response, accessToken, refreshToken);
//        userRepository.findByUserId(id).ifPresent(
//                user -> user.updateRefreshToken(refreshToken)
//        );
//
//        log.info("로그인 성공 id: " + id);
//        log.info("AccessToken: " + accessToken);
//        log.info("RefreshToken: " + refreshToken);
//
//
//        response.getWriter().write("성공");
//    }
//
//    private String extractId(Authentication authentication) {
//        UserPrincipal userprincipal = (UserPrincipal) authentication.getPrincipal();
//        return userprincipal.getUsername();
//    }
//}
