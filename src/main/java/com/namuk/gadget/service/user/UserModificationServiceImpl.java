package com.namuk.gadget.service.user;

import com.namuk.gadget.controller.security.config.SecurityConfig;
import com.namuk.gadget.domain.User;
import com.namuk.gadget.dto.member.ChangePasswordRequestDTO;
import com.namuk.gadget.repository.member.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserModificationServiceImpl implements UserModificationService {

    private final static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private final UserRepository userRepository;

    @Autowired
    public UserModificationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public String updatePassword(ChangePasswordRequestDTO changePasswordRequestDTO, String id) {
        User user = userRepository.findByUserId(id)
                .orElseThrow(IllegalArgumentException::new);
        if (encoder.matches(changePasswordRequestDTO.getCurrentPassword(), user.getUserPassword())) {
            if (!encoder.matches(changePasswordRequestDTO.getNewPassword(), user.getUserPassword())) {
                user.updatePassword(encoder.encode(changePasswordRequestDTO.getNewPassword()));
                userRepository.save(user);
                return "비밀번호 변경 완료: " + changePasswordRequestDTO.getNewPassword();
            } else {
                throw new IllegalArgumentException("The new password must be different from the current password");
            }
        } else {
            throw new IllegalArgumentException("The current password is incorrect");
        }
    }
}
