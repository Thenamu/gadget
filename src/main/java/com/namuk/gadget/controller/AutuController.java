package com.namuk.gadget.controller;

import com.namuk.gadget.security.jwt.JwtPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AutuController {

    @GetMapping(value = "/Auth/login")
    public String authLogin(@AuthenticationPrincipal JwtPrincipal jwtPrincipal) {
        return "If you see this, then you're logged in as user " + jwtPrincipal.getUsername();
    }

    @GetMapping(value = "/admin")
    public String adminLogin(@AuthenticationPrincipal JwtPrincipal jwtPrincipal) {
        return "Admin Login is Success" + " User ID: " + jwtPrincipal.getUsername();
    }
}
