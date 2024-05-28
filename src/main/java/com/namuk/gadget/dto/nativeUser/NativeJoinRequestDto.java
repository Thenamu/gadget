package com.namuk.gadget.dto.nativeUser;

import com.namuk.gadget.domain.Native;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NativeJoinRequestDto {

    @NotBlank(message = "아이디가 비어 있습니다.")
    private String nativeId;

    @NotBlank(message = "비밀번호가 비어 있습니다.")
    private String nativePassword;

    @NotBlank(message = "전화번호가 비어 있습니다.")
    private String nativePhoneNumber;

    @NotBlank(message = "이메일이 비어 있습니다.")
    private String nativeEmail;

    @NotBlank(message = "사는 지역이 비어 있습니다.")
    private String nativeLocation;

    @NotBlank(message = "프로필이 비어 있습니다.")
    private String nativeProfile;

    private String nativeGender;

    public Native toNativeEntity() {
        return Native.builder()
                .nativeId(this.nativeId)
                .nativePassword(this.nativePassword)
                .nativePhoneNumber(this.nativePhoneNumber)
                .nativeEmail(this.nativeEmail)
                .nativeLocation(this.nativeLocation)
                .nativeProfile(this.nativeProfile)
                .nativeGender(this.nativeGender)
                .build();
    }
}
