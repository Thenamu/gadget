package com.namuk.gadget.security.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.namuk.gadget.domain.User;
import com.namuk.gadget.repository.member.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * DTO 객체
 */
@Getter
@Builder
@AllArgsConstructor
public class JwtPrincipal implements UserDetails {

    private final String name;

    @JsonIgnore
    private final String password;

    // private final Long number;

    private final Collection<? extends GrantedAuthority> authorities;

//    public JwtPrincipal(User user) {
//        this.user = user;
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
//        Collection<GrantedAuthority> collection =  new ArrayList<>();
//
//        collection.add(new GrantedAuthority() {
//
//            @Override
//            public String getAuthority() {
//                return "ROLE_USER";
//            }
//        });
//
//        return collection;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public String getPassword() {
        return password;
    }

//    // public Long getUserNumber() {
//        return number;
//    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
