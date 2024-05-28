package com.namuk.gadget.dto.login;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {

    private String id;

    private String name;

    private String authority;
}
