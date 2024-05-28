package com.namuk.gadget.dto.member;

import com.namuk.gadget.domain.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

@Data
public class MemberJoinRequestDTO {

    @NotBlank(message = "아이디가 비어 있습니다.")
    private String userId;

    @NotBlank(message = "비밀번호가 비어 있습니다.")
    private String userPassword;

    private LocalDate userBirthDate;

    private String userPhoneNumber;

    @NotBlank(message = "성별이 비어 있습니다.")
    private String userGender;

    @NotBlank(message = "이름이 비어 있습니다.")
    private String userName;

    private String userEmail;

    private String userSns;

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public User toUserEntity() {
        return User.builder()
                .userId(this.userId)
                .userPassword(bCryptPasswordEncoder.encode(this.userPassword))
                .userBirthDate(this.userBirthDate)
                .userPhoneNumber(this.userPhoneNumber)
                .userGender(this.userGender)
                .userName(this.userName)
                .userEmail(this.userEmail)
                .userSns(this.userSns)
                .build();
    }
}
