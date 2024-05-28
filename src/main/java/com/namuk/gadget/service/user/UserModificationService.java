package com.namuk.gadget.service.user;

import com.namuk.gadget.domain.User;
import com.namuk.gadget.dto.member.ChangePasswordRequestDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserModificationService {

    String updatePassword(ChangePasswordRequestDTO changePasswordRequestDTO, String id);
}
