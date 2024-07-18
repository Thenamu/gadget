package com.namuk.gadget.security.userDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * UserDetails 구현
 */
@Getter
@Builder
@AllArgsConstructor
public class UserPrincipal implements UserDetails {

    private final String id;

    private final String password;

    private final String name;

    private final Collection<? extends GrantedAuthority> authorities;

    /**
     * @return 사용자에게 부여된 권한 목록 반환
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * @return 사용자를 식별할 수 있는 사용자 이름(아이디) 반환(사용되는 값은 반드시 고유해야 함)
     */
    @Override
    public String getUsername() {
        return id;
    }

    /**
     * @return 사용자의 비밀번호 반환(반드시 암호화해서 저장)
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * @return 계정이 만료되었는지 확인, 만료되지 않았을 시 true 반환
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * @return 계정이 잠금되었는지 확인, 잠금되지 않았을 시 true 반환
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * @return 비밀번호가 만료되었는지 확인, 만료되지 않았을 시 true 반환
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * @return 계정이 사용 가능한지 확인, 사용 가능할 시 true 반환
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
