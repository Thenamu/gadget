package com.namuk.gadget.dto.member;

import lombok.Data;

@Data
public class ChangePasswordRequestDTO {

    private String currentPassword;

    private String newPassword;
}
