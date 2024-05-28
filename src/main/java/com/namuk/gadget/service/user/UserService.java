package com.namuk.gadget.service.user;

import com.namuk.gadget.domain.User;
import com.namuk.gadget.dto.login.LoginRequestDTO;
import com.namuk.gadget.dto.member.MemberJoinRequestDTO;

public interface UserService {

    boolean checkForDuplicateLoginId(String id);

    boolean checkForDuplicateEmail(String email);

    boolean checkForDuplicatePhoneNumber(String phoneNumber);


//  boolean checkForDuplicateFields(MemberJoinRequestDto memberJoinRequestDto);

    User memberLogin(LoginRequestDTO loginRequestDTO);

    // Optional<User> memberLogin2(MemberLoginDto memberLoginDto);

    String memberJoin(MemberJoinRequestDTO memberJoinRequestDto);
}
