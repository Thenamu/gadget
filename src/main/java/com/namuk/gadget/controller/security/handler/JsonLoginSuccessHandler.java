package com.namuk.gadget.controller.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.namuk.gadget.controller.security.userDetails.UserPrincipal;
import com.namuk.gadget.dto.login.LoginResponseDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

public class JsonLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(principal.getId(), principal.getName(), principal.getAuthorities().toString());

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonPrincipal = objectMapper.writeValueAsString(loginResponseDTO);

        System.out.println("로그인 성공 jsonPrincipal = " + jsonPrincipal);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonPrincipal);
    }
}
