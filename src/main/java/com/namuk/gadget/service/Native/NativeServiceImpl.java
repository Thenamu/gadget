package com.namuk.gadget.service.Native;

import com.namuk.gadget.domain.Native;
import com.namuk.gadget.dto.login.LoginRequestDTO;
import com.namuk.gadget.dto.nativeUser.NativeJoinRequestDto;
import com.namuk.gadget.repository.Native.NativeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NativeServiceImpl implements NativeService{

    private final NativeRepository nativeRepository;

    @Autowired
    public NativeServiceImpl(NativeRepository nativeRepository) {
        this.nativeRepository = nativeRepository;
    }

    @Override
    public boolean checkForDuplicateLoginId(String id) {
        return nativeRepository.existsByNativeId(id);
    }

    @Override
    public boolean checkForDuplicateEmail(String email) {
        return nativeRepository.existsByNativeEmail(email);
    }

    @Override
    public boolean checkForDuplicatePhoneNumber(String phoneNumber) {
        return nativeRepository.existsByNativePhoneNumber(phoneNumber);
    }

    @Override
    public String nativeJoin(NativeJoinRequestDto nativeJoinRequestDto) {
        nativeRepository.save(nativeJoinRequestDto.toNativeEntity());
        return nativeJoinRequestDto.getNativeId();
    }

    @Override
    public Native nativeLogin(LoginRequestDTO loginRequestDTO) {
        Native nativeInfo = nativeRepository.findByNativeId(loginRequestDTO.getId());
        return nativeInfo;

        // return nativeRepository.findByNativeId(nativeLoginDTO.getNativeLoginId());
    }
}
