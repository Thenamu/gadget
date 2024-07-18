package com.namuk.gadget.dto.member;

import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Data
public class JwtAuthenticationDTO {

    // private Long number;

    private String id;

    private String password;

    private List<SimpleGrantedAuthority> role;
}
