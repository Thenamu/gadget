package com.namuk.gadget.service.Native;

import com.namuk.gadget.domain.Native;
import com.namuk.gadget.dto.login.LoginRequestDTO;
import com.namuk.gadget.dto.nativeUser.NativeJoinRequestDto;

public interface NativeService {

    boolean checkForDuplicateLoginId(String id);

    boolean checkForDuplicateEmail(String email);

    boolean checkForDuplicatePhoneNumber(String phoneNumber);

    String nativeJoin(NativeJoinRequestDto nativeJoinRequestDto);

    Native nativeLogin(LoginRequestDTO loginRequestDTO);
}
