package com.namuk.gadget.security.jwt;

import com.namuk.gadget.domain.User;
import com.namuk.gadget.repository.member.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomJwtUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomJwtUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String id) {
        User user = userRepository.findByUserId(id)
                .orElseThrow(() -> new UsernameNotFoundException("Not found User with id: " + id));
        return JwtPrincipal.builder()
                .name(user.getUserId())
                .password(user.getUserPassword())
                // .number(user.getUserNumber())
                // List.of(new SimpleGrantedAuthority(user.getUserRole())) User Entity에서 권한을 가져와서 문자열 객체를 만든 후 불변 리스트에 저장
                .authorities(List.of(new SimpleGrantedAuthority(user.getUserRole())))
                .build();
    }
}
