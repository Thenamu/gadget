package com.namuk.gadget.security.userDetailService;

import com.namuk.gadget.security.userDetails.UserPrincipal;
import com.namuk.gadget.domain.Native;
import com.namuk.gadget.domain.User;
import com.namuk.gadget.repository.Native.LocalRepository;
import com.namuk.gadget.repository.member.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * UserDetailService 구현
 */
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    private final LocalRepository localRepository;

    @Autowired
    public CustomUserDetailService(UserRepository userRepository, LocalRepository localRepository) {
        this.userRepository = userRepository;
        this.localRepository = localRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String id) {

        if(userRepository.findByUserId(id).isPresent()) {
            User user = userRepository.findByUserId(id)
                    .orElseThrow(() -> new UsernameNotFoundException("Not found User"));
            return UserPrincipal.builder()
                    .id(user.getUserId())
                    .password(user.getUserPassword())
                    .name(user.getUserName())
                    .authorities(List.of(new SimpleGrantedAuthority("ROLE_USER"))) // Collections.singleton()
                    .build();
        } else if(localRepository.findByNativeId(id).isPresent()) {
            Native aNative = localRepository.findByNativeId(id)
                    .orElseThrow(() -> new UsernameNotFoundException("Not found Native"));
            return UserPrincipal.builder()
                    .id(aNative.getNativeId())
                    .password(aNative.getNativePassword())
                    .name(aNative.getNativeProfile())
                    .authorities(List.of(new SimpleGrantedAuthority("ROLE_NATIVE")))
                    .build();
        }

//         return new UserPrincipal (
//                    user.getUserId(),
//                    user.getUserPassword(),
//                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
//                );

        return null;
    }
}
